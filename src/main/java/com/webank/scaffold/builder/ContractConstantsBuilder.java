package com.webank.scaffold.builder;

import com.squareup.javapoet.*;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/05/12
 */
public class ContractConstantsBuilder implements JavaFileBuilder {

    private List<String> contracts;
    private UserConfig config;


    public ContractConstantsBuilder(UserConfig config, List<String> contracts){
        this.contracts = contracts;
        this.config = config;
    }

    @Override
    public String getJavaFilePackage(String relativePackage) {
        return config.getGroup() + "." + config.getArtifact() + relativePackage;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String fullPkg) {
        /**
         * 1. Create class header
         */
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(ClassName.get(fullPkg, FileNameConstants.CONTRACT_CONSTANTS))
                .addModifiers(Modifier.PUBLIC);

        /**
         * 2. Create fields
         */
        for(String contract:contracts){
            typeBuilder.addField(abiField(contract));
            typeBuilder.addField(binaryField(contract));
            typeBuilder.addField(gmBinaryField(contract));
        }

        /**
         * 3. Static blocks for field initializers
         */
        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder()
                .beginControlFlow("try");
        String readUtilFullName =
                "org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource(\"$L/$L\"))";
        for(String contract:contracts) {
            String abiFieldName = contract + FileNameConstants.ABI_POSTFIX;
            staticBlockBuilder.addStatement(abiFieldName + " = " + readUtilFullName, DirNameConstants.ABI_DIR, contract + ".abi");
            String binFieldName = contract + FileNameConstants.BINARY_POSTFIX;
            staticBlockBuilder.addStatement(binFieldName + " = " + readUtilFullName, DirNameConstants.BIN_DIR, contract + ".bin");
            String gmBinFieldName = contract + FileNameConstants.BINARY_GM_POSTFIX;
            staticBlockBuilder.addStatement(gmBinFieldName + " = " + readUtilFullName, DirNameConstants.SMBIN_DIR, contract + ".bin");
        }

        staticBlockBuilder
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();
        typeBuilder.addStaticBlock(staticBlockBuilder.build());

        return Arrays.asList(typeBuilder.build());
    }

    private FieldSpec abiField(String contract){
        String fieldName = contract+FileNameConstants.ABI_POSTFIX;
        FieldSpec fieldSpec = FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC, Modifier.STATIC).build();
        return fieldSpec;
    }

    private FieldSpec binaryField(String contract){
        String fieldName = contract+FileNameConstants.BINARY_POSTFIX;
        FieldSpec fieldSpec = FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC, Modifier.STATIC).build();
        return fieldSpec;
    }

    private FieldSpec gmBinaryField(String contract){
        String fieldName = contract+FileNameConstants.BINARY_GM_POSTFIX;
        FieldSpec fieldSpec = FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC, Modifier.STATIC).build();
        return fieldSpec;
    }
}
