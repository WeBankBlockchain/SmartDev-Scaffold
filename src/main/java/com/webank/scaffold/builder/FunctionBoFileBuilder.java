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

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.ABIUtil;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FunctionBoFileHandler
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 11:30
 **/
public class FunctionBoFileBuilder implements JavaFileBuilder {

    private String contractName;
    private String abiStr;
    private UserConfig config;

    public FunctionBoFileBuilder(String contractName, String abiStr, UserConfig config){
        this.contractName = contractName;
        this.abiStr = abiStr;
        this.config = config;
    }

    @Override
    public String getJavaFilePackage(String pkgName) {
        return config.getGroup() + "." + config.getArtifact() + pkgName;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String pkg) {
        Map<ABIDefinition,TypeSpec> result = ABIUtil.buildAbiDefAndTypeSpec(contractName,abiStr);
        return result.values().stream().collect(Collectors.toList());
    }
}
