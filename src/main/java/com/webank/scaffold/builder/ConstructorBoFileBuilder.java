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
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.util.ABIUtil;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinitionFactory;
import org.fisco.bcos.sdk.abi.wrapper.ContractABIDefinition;
import org.fisco.bcos.sdk.crypto.CryptoSuite;

import java.util.Arrays;
import java.util.List;

/**
 * BoFileHandler
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 10:47
 **/
public class ConstructorBoFileBuilder implements JavaFileBuilder {

    private String contractName;
    private String abiStr;
    private UserConfig config;

    public ConstructorBoFileBuilder(String contractName, String abiStr, UserConfig config){
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

        // 1.Get Constructor ABIDefinition
        ABIDefinitionFactory factory = new ABIDefinitionFactory(new CryptoSuite(0));
        ContractABIDefinition rootAbi = factory.loadABI(abiStr);
        ABIDefinition constructorAbi = rootAbi.getConstructor();

        // 2.Returns null if no constructor defined(this is the common case)
        if(constructorAbi == null || constructorAbi.getInputs().isEmpty()) return null;

        // 3.Build TypeSpec
        String className = this.contractName + FileNameConstants.CTOR;
        return Arrays.asList(ABIUtil.buildBOType(className, constructorAbi.getInputs()));
    }
}
