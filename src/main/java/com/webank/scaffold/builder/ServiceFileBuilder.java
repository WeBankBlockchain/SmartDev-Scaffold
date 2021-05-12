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
import com.webank.scaffold.handler.ServicesHandler;
import com.webank.scaffold.util.ABIUtil;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ServiceFileHandler
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 15:44
 **/
public class ServiceFileBuilder implements JavaFileBuilder {

    private String contractName;
    private String abiStr;
    private UserConfig config;

    public ServiceFileBuilder(String contractName, String abiStr, UserConfig config){
        this.contractName = contractName;
        this.abiStr = abiStr;
        this.config = config;
    }

    @Override
    public String getJavaFilePackage(String relativePackage) {
        return config.getGroup() + "." + config.getArtifact() + relativePackage;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String fullPkg) {
        Map<ABIDefinition, TypeSpec> functions = ABIUtil.buildAbiDefAndTypeSpec(contractName, abiStr);
        ServicesHandler servicesHandler = new ServicesHandler(config);
        TypeSpec serviceType = servicesHandler.build(contractName, functions);
        return Arrays.asList(serviceType);
    }
}
