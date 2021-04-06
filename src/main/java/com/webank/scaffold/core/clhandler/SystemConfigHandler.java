package com.webank.scaffold.core.clhandler;

import com.squareup.javapoet.*;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.CommonUtil;
import com.webank.scaffold.core.util.PackageNameUtil;
import lombok.Data;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/03
 */
public class SystemConfigHandler {

    public static final String SYSTEN_CONFIG = "SystemConfig";
    private static final String CONTRACT = "ContractConfig";

    private File javaRoot;
    private List<String> contracts;
    private UserConfig config;

    public SystemConfigHandler(File javaRoot, List<String> contracts, UserConfig config){
        this.javaRoot = javaRoot;
        this.contracts = contracts;
        this.config = config;
    }

    public void export() throws IOException {
        String pkg = PackageNameUtil.getConfigPackageName(config);
        //1. Make Contract class
        TypeSpec contractConfigClass = createContractConfigClass(pkg);
        //2. Make SystemConfig class
        TypeSpec systemConfigClass = createSystemConfigClass(contractConfigClass, pkg);
        //3. Output
        JavaFile contractConfigFile = JavaFile.builder(pkg, contractConfigClass).build();
        contractConfigFile.writeTo(this.javaRoot);
        JavaFile systemConfigFile = JavaFile.builder(pkg, systemConfigClass).build();
        systemConfigFile.writeTo(this.javaRoot);
    }

    private TypeSpec createContractConfigClass(String pkg) {
        //1. Basic initializations
        ClassName contractConfigClass = ClassName.get(pkg, CONTRACT);
        TypeSpec.Builder contractConfigBuilder = TypeSpec.classBuilder(contractConfigClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class);

        //2. Populate contract fields
        for (String contract : this.contracts) {
            String fieldName = CommonUtil.makeFirstCharLowerCase(contract)+"Address";
            contractConfigBuilder.addField(String.class, fieldName, Modifier.PRIVATE);
        }

        return contractConfigBuilder.build();
    }

    private TypeSpec createSystemConfigClass(TypeSpec contractClass, String pkg) {
        ClassName className = ClassName.get(pkg, SYSTEN_CONFIG);
        TypeSpec systemConfigBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(ClassName.get("org.springframework.context.annotation","Configuration"))
                .addAnnotation(
                        AnnotationSpec
                                .builder(ClassName.get("org.springframework.boot.context.properties","ConfigurationProperties"))
                                .addMember("prefix", "\"system\"")
                                .build())
                .addField(
                        FieldSpec
                                .builder(
                                String.class, "peers", Modifier.PRIVATE)
                                .build())
                .addField(
                        FieldSpec
                                .builder(
                                        TypeName.INT, "groupId", Modifier.PRIVATE)
                                .initializer("1")
                                .build())
                .addField(
                        FieldSpec
                                .builder(
                                        String.class, "certPath", Modifier.PRIVATE)
                                .initializer("\"conf\"")
                                .build())
                .addField(
                        FieldSpec
                                .builder(
                                        String.class, "hexPrivateKey", Modifier.PRIVATE)
                                .build())
                .addField(
                        FieldSpec
                                .builder(ClassName.get(pkg, contractClass.name), "contract", Modifier.PRIVATE)
                                .addAnnotation(ClassName.get("org.springframework.boot.context.properties","NestedConfigurationProperty"))
                                .build())
                .build();
        return systemConfigBuilder;
    }
}
