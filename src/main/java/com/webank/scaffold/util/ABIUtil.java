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
package com.webank.scaffold.util;

import com.squareup.javapoet.*;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.handler.SolidityTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinitionFactory;
import org.fisco.bcos.sdk.abi.wrapper.ContractABIDefinition;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ABIUtil
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-08 15:48
 **/
public class ABIUtil {

    public static TypeSpec buildBOType(String className, List<ABIDefinition.NamedType> args){

        // 1.Check: No need to generate BO for functions with no args
        if(args.isEmpty()) return null;//No need

        // 2.metadata
        TypeSpec.Builder boBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(AllArgsConstructor.class);

        // 3.Fields
        int argIndex = 0;
        for(ABIDefinition.NamedType namedType: args){
            String argName = namedType.getName();
            if(argName == null || argName.isEmpty()){
                argName = "arg"+argIndex;
            }
            String typeString = namedType.getTypeAsString();
            TypeName type = SolidityTypeHandler.convert(typeString);
            boBuilder.addField(type, argName, Modifier.PRIVATE);
            argIndex++;
        }

        // 4.Methods
        MethodSpec.Builder toArgsMethodBuilder = MethodSpec.methodBuilder("toArgs")
                .addModifiers(Modifier.PUBLIC)
                .returns(ListObject.class.getGenericInterfaces()[0])
                .addStatement("$T args = new $T()", List.class, ArrayList.class);
        for(ABIDefinition.NamedType arg: args){
            toArgsMethodBuilder.addStatement("args.add($L)", arg.getName());
        }
        toArgsMethodBuilder.addStatement("return args");
        boBuilder.addMethod(toArgsMethodBuilder.build());

        TypeSpec boType = boBuilder.build();
        return boType;
    }

    public static Map<ABIDefinition, TypeSpec> buildAbiDefAndTypeSpec(String contractName, String abiStr) {

        ABIDefinitionFactory factory = new ABIDefinitionFactory(new CryptoSuite(0));
        ContractABIDefinition rootAbi = factory.loadABI(abiStr);
        Map<String, List<ABIDefinition>> functions = rootAbi.getFunctions();

        Map<ABIDefinition,TypeSpec> result = new HashMap<>();
        for(Map.Entry<String, List<ABIDefinition>> e: functions.entrySet()){
            List<ABIDefinition> definitions = e.getValue();
            for(int i=0;i<definitions.size();i++){
                ABIDefinition abiDef = definitions.get(i);
                String functionName = StringUtils.capitaliseFirstLetter(abiDef.getName());
                String overloadMark = i>0?Integer.toString(i):"";
                String className = contractName
                        + functionName
                        + overloadMark
                        + FileNameConstants.INPUT;
                TypeSpec inputType = buildBOType(className, abiDef.getInputs());
                result.put(abiDef, inputType);
            }
        }
        return result;
    }

    public static List<String> contracts(File abiDir, String need){
        Set<String> needContracts = Arrays.stream(need.split(",|;")).collect(Collectors.toSet());
        List<String> contractList = Arrays.stream(abiDir.listFiles())
                .map(f->f.getName().split("\\.")[0])
                .filter(c-> (need==null||need.isEmpty()) || needContracts.contains(c))
                .collect(Collectors.toList());
        return contractList;
    }

    interface ListObject extends List<Object> {
    }
}
