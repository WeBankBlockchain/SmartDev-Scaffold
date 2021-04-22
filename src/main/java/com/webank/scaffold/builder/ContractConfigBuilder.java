/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.scaffold.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.FileNameConstants;
import lombok.Data;
import org.fisco.bcos.sdk.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * ContractConfigHander
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 09:33
 **/
public class ContractConfigBuilder implements JavaFileBuilder {

    private List<String> contracts;
    private UserConfig config;

    public ContractConfigBuilder(UserConfig config, List<String> contracts){
        this.contracts = contracts;
        this.config = config;
    }

    @Override
    public String getJavaFilePackage(String pkgName) {
        return config.getGroup() + "." + config.getArtifact() + pkgName;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String pkg) {
        //1. Basic initializations
        ClassName contractConfigClass = ClassName.get(pkg, FileNameConstants.CONTRACT_CONFIG);
        TypeSpec.Builder contractConfigBuilder = TypeSpec.classBuilder(contractConfigClass)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(ClassName.get("org.springframework.context.annotation","Configuration"))
                .addAnnotation(
                        AnnotationSpec
                                .builder(ClassName.get("org.springframework.boot.context.properties","ConfigurationProperties"))
                                .addMember("prefix", "\"contract\"")
                                .build());

        //2. Populate contract fields
        for (String contract : this.contracts) {
            String fieldName = StringUtils.lowercaseFirstLetter(contract) +"Address";
            contractConfigBuilder.addField(String.class, fieldName, Modifier.PRIVATE);
        }
        return Arrays.asList(contractConfigBuilder.build());
    }
}
