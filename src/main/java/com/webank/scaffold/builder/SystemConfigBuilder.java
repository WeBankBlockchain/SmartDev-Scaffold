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

import com.squareup.javapoet.*;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.FileNameConstants;
import lombok.Data;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * SystemConfigBuilder
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 10:10
 **/
public class SystemConfigBuilder implements JavaFileBuilder {

    private UserConfig config;

    public SystemConfigBuilder(UserConfig config){
        this.config = config;
    }

    @Override
    public String getJavaFilePackage(String pkgName) {
        return config.getGroup() + "." + config.getArtifact() + pkgName;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String pkg) {
        ClassName className = ClassName.get(pkg, FileNameConstants.SYSTEM_CONFIG);
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
                .build();

        return Arrays.asList(systemConfigBuilder);
    }
}
