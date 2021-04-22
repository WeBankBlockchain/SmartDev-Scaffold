package com.webank.scaffold.handler;

import com.squareup.javapoet.*;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/27
 */
public class ServicesHandler {

    private static final String ABI = "ABI";
    private static final String BIN = "BINARY";
    private static final String SMBIN = "SM_BINARY";

    private UserConfig config;

    public ServicesHandler(UserConfig config){
        this.config = config;
    }

    public TypeSpec build(String contract, Map<ABIDefinition, TypeSpec> functionWithInputBO){

        // 1. create service class
        ClassName serviceClassName = serviceClassName(contract);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(serviceClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.stereotype","Service"))
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(Data.class);

        // 2. Add static field: ABI | BIN | SM_BIN
        typeBuilder = this.populateStaticFields(contract, typeBuilder);

        // 3. Instance fields: String address|Client client |AssemblyTransactionProcessor processor
        typeBuilder=  this.populateInstanceFields(contract, typeBuilder);

        // 4. Add post constructor
        typeBuilder = this.populateInitializer(contract, typeBuilder);

        // 5. Add method: transaction | call
        typeBuilder = this.populateMethods(typeBuilder, functionWithInputBO);
        return typeBuilder.build();
    }


    private ClassName serviceClassName(String contract){
        String pkg = config.getGroup() + "." + config.getArtifact();
        String serviceName = contract + "Service";
        return ClassName.get(pkg+".service", serviceName);
    }

    private TypeSpec.Builder populateStaticFields(String contract, TypeSpec.Builder typeBuilder){
        String utilsPackage = config.getGroup() + "." + config.getArtifact() + ".utils";
        String ioUtilFullName = utilsPackage+".IOUtil";
        FieldSpec abiField
                = FieldSpec
                .builder(String.class, ABI)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(ioUtilFullName + ".readResourceAsString(\"$L/$L\")", DirNameConstants.ABI_DIR, contract+".abi")
                .build();

        FieldSpec bin
                = FieldSpec
                .builder(String.class, BIN)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(ioUtilFullName + ".readResourceAsString(\"$L/$L\")", DirNameConstants.BIN_DIR, contract+".bin")
                .build();

        FieldSpec smbin
                = FieldSpec
                .builder(String.class, SMBIN)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(ioUtilFullName + ".readResourceAsString(\"$L/$L\")", DirNameConstants.SMBIN_DIR, contract+".bin")
                .build();

        return typeBuilder.addField(abiField).addField(bin).addField(smbin);
    }

    private TypeSpec.Builder populateInstanceFields(String contract, TypeSpec.Builder typeBuilder){
        FieldSpec addressField
                = FieldSpec.builder(String.class, "address")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.beans.factory.annotation","Value"))
                        .addMember("value","\"$${system.contract.$LAddress}\"", StringUtils.lowercaseFirstLetter(contract)).build())
                .build();
        FieldSpec clientField
                = FieldSpec.builder(Client.class, "client")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation","Autowired"))
                .build();
        FieldSpec processField
                = FieldSpec.builder(AssembleTransactionProcessor.class, "txProcessor")
                .build();
        return typeBuilder.addField(addressField).addField(clientField).addField(processField);
    }

    private TypeSpec.Builder populateInitializer(String contract, TypeSpec.Builder typeBuilder){
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder("init");
        txBuilder.addModifiers(Modifier.PUBLIC).addAnnotation(ClassName.get("javax.annotation","PostConstruct")).addException(Exception.class);
        txBuilder
                .addStatement("this.txProcessor = $T.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair())", TransactionProcessorFactory.class);
        typeBuilder.addMethod(txBuilder.build());
        return typeBuilder;
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
        String pkg = config.getGroup() + "." + config.getArtifact() + FileNameConstants.BO_PKG_POSTFIX;
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
        String pkg = config.getGroup() + "." + config.getArtifact() + FileNameConstants.BO_PKG_POSTFIX;
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
}
