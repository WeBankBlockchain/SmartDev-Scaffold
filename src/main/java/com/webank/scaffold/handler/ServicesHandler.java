package com.webank.scaffold.handler;

import java.util.Arrays;
import java.util.Map;

import javax.lang.model.element.Modifier;

import com.webank.scaffold.builder.ContractConstantsBuilder;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.utils.StringUtils;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/27
 */
public class ServicesHandler {

    private UserConfig config;

    public ServicesHandler(UserConfig config) {
        this.config = config;
    }

    public TypeSpec build(String contract, Map<ABIDefinition, TypeSpec> functionWithInputBO) {

        // 1. create service class
        ClassName serviceClassName = serviceClassName(contract);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(serviceClassName).addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.stereotype", "Service"))
                .addAnnotation(NoArgsConstructor.class).addAnnotation(Data.class);

        // 2. Instance fields: String address|Client client |AssemblyTransactionProcessor processor
        typeBuilder = this.populateInstanceFields(contract, typeBuilder);

        // 3. Add post constructor
        typeBuilder = this.populateInitializer(contract, typeBuilder);

        // 4. Add method: transaction | call
        typeBuilder = this.populateMethods(contract, typeBuilder, functionWithInputBO);
        return typeBuilder.build();
    }

    private ClassName serviceClassName(String contract) {
        String pkg = config.getGroup() + "." + config.getArtifact();
        String serviceName = contract + "Service";
        return ClassName.get(pkg + ".service", serviceName);
    }

    private TypeSpec.Builder populateInstanceFields(String contract, TypeSpec.Builder typeBuilder) {
        FieldSpec addressField = FieldSpec.builder(String.class, "address").addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec
                        .builder(ClassName.get("org.springframework.beans.factory.annotation", "Value"))
                        .addMember("value", "\"$${contract.$LAddress}\"", StringUtils.lowercaseFirstLetter(contract))
                        .build())
                .build();
        FieldSpec clientField = FieldSpec.builder(Client.class, "client").addModifiers(Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired")).build();
        FieldSpec processField = FieldSpec.builder(AssembleTransactionProcessor.class, "txProcessor").build();
        return typeBuilder.addField(addressField).addField(clientField).addField(processField);
    }

    private TypeSpec.Builder populateInitializer(String contract, TypeSpec.Builder typeBuilder) {
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder("init");
        txBuilder.addModifiers(Modifier.PUBLIC).addAnnotation(ClassName.get("javax.annotation", "PostConstruct"))
                .addException(Exception.class);
        txBuilder.addStatement(
                "this.txProcessor = $T.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair())",
                TransactionProcessorFactory.class);
        typeBuilder.addMethod(txBuilder.build());
        return typeBuilder;
    }

    private TypeSpec.Builder populateMethods(String contract, TypeSpec.Builder typeBuilder,
            Map<ABIDefinition, TypeSpec> functionWithInputBO) {

        for (Map.Entry<ABIDefinition, TypeSpec> entrySet : functionWithInputBO.entrySet()) {
            ABIDefinition function = entrySet.getKey();
            TypeSpec inputBO = entrySet.getValue();
            if (!function.isConstant()) {
                typeBuilder.addMethod(getTransactionMethod(contract, function.getName(), inputBO));
            } else {
                typeBuilder.addMethod(getCallMethod(contract, function.getName(), inputBO));
            }
        }

        return typeBuilder;
    }

    private MethodSpec getTransactionMethod(String contract, String function, TypeSpec inputType) {
        ClassName constantsClass = constantsClassName();
        String abiField = contract+FileNameConstants.ABI_POSTFIX;
        String pkg = config.getGroup() + "." + config.getArtifact() + FileNameConstants.BO_PKG_POSTFIX;
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder(function);
        txBuilder.addModifiers(Modifier.PUBLIC).addException(Exception.class).returns(TransactionResponse.class);
        if (inputType != null) {
            txBuilder.addParameter(ClassName.get(pkg, inputType.name), "input").addStatement(
                    "return this.txProcessor.sendTransactionAndGetResponse(this.address, $T.$L, \"$L\", input.toArgs())",
                    constantsClass, abiField,function);
        } else {
            txBuilder.addStatement(
                    "return this.txProcessor.sendTransactionAndGetResponse(this.address, $T.$L, \"$L\", $T.asList())",
                    constantsClass, abiField, function, Arrays.class);
        }
        return txBuilder.build();
    }

    private MethodSpec getCallMethod(String contract, String function, TypeSpec inputType) {
        ClassName constantsClass = constantsClassName();
        String abiField = contract+FileNameConstants.ABI_POSTFIX;
        String pkg = config.getGroup() + "." + config.getArtifact() + FileNameConstants.BO_PKG_POSTFIX;
        MethodSpec.Builder txBuilder = MethodSpec.methodBuilder(function);
        txBuilder.addModifiers(Modifier.PUBLIC).addException(Exception.class).returns(CallResponse.class);
        if (inputType != null) {
            txBuilder.addParameter(ClassName.get(pkg, inputType.name), "input").addStatement(
                    "return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, $T.$L, \"$L\", input.toArgs())",
                    constantsClass, abiField, function);
        } else {
            txBuilder.addStatement(
                    "return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, $T.$L, \"$L\", $T.asList())",
                    constantsClass, abiField, function, Arrays.class);
        }
        return txBuilder.build();
    }


    private ClassName constantsClassName() {
        String pkg = new ContractConstantsBuilder(config, Arrays.asList()).getJavaFilePackage(FileNameConstants.CONSTANT_PKG_POSTFIX);
        String simpleName = "ContractConstants";
        return ClassName.get(pkg , simpleName);
    }
}
