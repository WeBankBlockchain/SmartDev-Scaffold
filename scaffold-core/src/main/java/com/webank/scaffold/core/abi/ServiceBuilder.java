package com.webank.scaffold.core.abi;

import com.squareup.javapoet.*;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.PackageNameUtil;
import lombok.Getter;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/27
 */
public class ServiceBuilder {

    private static final String ABI = "ABI";
    private static final String BIN = "BINARY";
    private static final String SMBIN = "SM_BINARY";

    private UserConfig config;

    public ServiceBuilder(UserConfig config){
        this.config = config;
    }

    public TypeSpec build(String contract, TypeSpec ctorInputType, Map<ABIDefinition, TypeSpec> functionWithInputBO){
        /**
         * 1. Service class
         */
        ClassName serviceClassName = serviceClassName(contract);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(serviceClassName).addModifiers(Modifier.PUBLIC);
        /**
         * 2. Add static field: ABI | BIN | SM_BIN
         */
        typeBuilder = this.populateStaticFields(contract, typeBuilder);
        /**
         * 3. Instance fields: String address|Client client |AssemblyTransactionProcessor processor
         */
        typeBuilder=  this.populateInstanceFields(typeBuilder);
        /**
         * 4. Add constructor: load |  deploy
         */
        typeBuilder = this.populateConstructors(typeBuilder, ctorInputType);
        /**
         * 5. Add method: transaction | call
         */
        typeBuilder = this.populateMethods(typeBuilder, functionWithInputBO);
        return typeBuilder.build();

    }

    private ClassName serviceClassName(String contract){
        String pkg = PackageNameUtil.getRootPackageName(config);
        String serviceName = contract + "Service";
        return ClassName.get(pkg+".service", serviceName);
    }

    private TypeSpec.Builder populateStaticFields(String contract, TypeSpec.Builder typeBuilder){
        String pkg = PackageNameUtil.getRootPackageName(config);
        String javaContractFullName = pkg+".contracts."+contract;
        FieldSpec abiField
                = FieldSpec
                .builder(String.class, ABI)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(javaContractFullName + "." + ABI)
                .build();

        FieldSpec bin
                = FieldSpec
                .builder(String.class, BIN)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(javaContractFullName+"." +BIN)
                .build();

        FieldSpec smbin
                = FieldSpec
                .builder(String.class, SMBIN)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(javaContractFullName+"." +BIN)
                .build();

        return typeBuilder.addField(abiField).addField(bin).addField(smbin);
    }

    private TypeSpec.Builder populateInstanceFields(TypeSpec.Builder typeBuilder){
        FieldSpec addressField
                = FieldSpec.builder(String.class, "address")
                .addAnnotation(Getter.class)
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec clientField
                = FieldSpec.builder(Client.class, "client")
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec processField
                = FieldSpec.builder(AssembleTransactionProcessor.class, "txProcessor")
                .build();
        return typeBuilder.addField(addressField).addField(clientField).addField(processField);
    }

    private TypeSpec.Builder populateConstructors(TypeSpec.Builder typeBuilder, TypeSpec ctorInputType) {
        //Ctor1
        MethodSpec ctor1 = MethodSpec.constructorBuilder().addParameter(String.class, "address")
                .addParameter(Client.class, "client")
                .addException(Exception.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.client = client")
                .addStatement("this.txProcessor = $T.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair())", TransactionProcessorFactory.class)
                .addStatement("this.address = address")
                .build();
        //Ctor2
        String pkg = PackageNameUtil.getBOPackageName(config);
        MethodSpec.Builder ctor2Builder = MethodSpec.constructorBuilder()
                .addParameter(Client.class, "client");
        ctor2Builder
                .addException(Exception.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.client = client")
                .addStatement("this.txProcessor = $T.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair())", TransactionProcessorFactory.class);
        if (ctorInputType != null) {
            ctor2Builder.addParameter(ClassName.get(pkg,ctorInputType.name),"input");
            ctor2Builder.addStatement("this.address = this.txProcessor.deployAndGetResponse(ABI,this.client.getCryptoType()==0?$L:$L, input.toArgs()).getContractAddress()",BIN, SMBIN);
        }
        else{
            ctor2Builder.addStatement("this.address = this.txProcessor.deployAndGetResponse(ABI,this.client.getCryptoType()==0?$L:$L).getContractAddress()",BIN, SMBIN);
        }

        MethodSpec ctor2 = ctor2Builder.build();
        return typeBuilder.addMethod(ctor1).addMethod(ctor2);
    }

    private TypeSpec.Builder populateMethods(TypeSpec.Builder typeBuilder, Map<ABIDefinition, TypeSpec> functionWithInputBO) {

        for (Map.Entry<ABIDefinition, TypeSpec> entrySet : functionWithInputBO.entrySet()) {
            ABIDefinition function = entrySet.getKey();
            TypeSpec inputBO = entrySet.getValue();
            if (!function.isConstant()) {
                typeBuilder.addMethod(getTransactionMethod(function.getName(), inputBO));
            } else {
                typeBuilder.addMethod(getCallMethod(function.getName(), inputBO));
            }
        }

        return typeBuilder;
    }

    private MethodSpec getTransactionMethod(String function, TypeSpec inputType){
        String pkg = PackageNameUtil.getBOPackageName(config);
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder(function);
        txBuilder.addModifiers(Modifier.PUBLIC).addException(Exception.class).returns(TransactionResponse.class);
        if(inputType != null) {
            txBuilder.addParameter(ClassName.get(pkg, inputType.name), "input")
                    .addStatement("return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, \"$L\", input.toArgs())", function);
        }
        else{
            txBuilder
                    .addStatement("return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, \"$L\", $T.asList())", function, Arrays.class);
        }
        return txBuilder.build();
    }

    private MethodSpec getCallMethod(String function, TypeSpec inputType){
        String pkg = PackageNameUtil.getBOPackageName(config);
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder(function);
        txBuilder.addModifiers(Modifier.PUBLIC).addException(Exception.class).returns(CallResponse.class);
        if(inputType != null) {
            txBuilder.addParameter(ClassName.get(pkg, inputType.name), "input")
                    .addStatement("return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, \"$L\", input.toArgs())", function);
        }
        else{
            txBuilder
                    .addStatement("return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, \"$L\", $T.asList())", function, Arrays.class);
        }
        return txBuilder.build();
    }

    public void exportBO(TypeSpec serviceType, File javaDir) throws IOException {
        if(serviceType == null) return;
        String pkgName = PackageNameUtil.getServicePackageName(config);
        JavaFile file = JavaFile.builder(pkgName, serviceType)
                .build();

        file.writeTo(javaDir);
    }
}
