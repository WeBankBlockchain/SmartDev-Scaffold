package com.webank.scaffold.core.clhandler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.CommonUtil;
import com.webank.scaffold.core.util.PackageNameUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinitionFactory;
import org.fisco.bcos.sdk.abi.wrapper.ContractABIDefinition;
import org.fisco.bcos.sdk.crypto.CryptoSuite;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create bo class of event.
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
public class BOHandler {

    private final static String CTOR = "CtorBO";
    private final static String INPUT = "InputBO";

    private UserConfig config;

    public BOHandler(UserConfig config){
        this.config = config;
    }

    public void exportBOs(File destDir, List<TypeSpec> bos) throws IOException {
        String pkg = PackageNameUtil.getBOPackageName(config);

        for(TypeSpec typeSpec: bos){
            if(typeSpec == null){continue;}
            JavaFile javaFile = JavaFile.builder(pkg, typeSpec)
                    .build();
            //Save it to dir
            javaFile.writeTo(destDir);
        }
    }

    /**
     * Convert ctor inputs into a BO. If there is no ctor, or the ctor does not needs inputs, then it returns null.
     * @param contractName
     * @param abiStr
     * @return
     */
    public TypeSpec buildCtorBO(String contractName, String abiStr){
        /**
         * 1、Get ABI
         */
        ABIDefinitionFactory factory = new ABIDefinitionFactory(new CryptoSuite(0));
        ContractABIDefinition rootAbi = factory.loadABI(abiStr);
        ABIDefinition ctorAbi = rootAbi.getConstructor();

        /**
         * 2、Returns null if no ctor defined(this is the common case)
         */
        if(ctorAbi == null || ctorAbi.getInputs().isEmpty()) return null;

        /**
         * 3、Build BO
         */
        String className = contractName + CTOR;
        return this.buildBOType(className, ctorAbi.getInputs());
    }


    public Map<ABIDefinition,TypeSpec> buildFunctionBO(String contractName, String abiStr) {
        ABIDefinitionFactory factory = new ABIDefinitionFactory(new CryptoSuite(0));
        ContractABIDefinition rootAbi = factory.loadABI(abiStr);
        Map<String, List<ABIDefinition>> functions = rootAbi.getFunctions();
        /**
         * 1. For each function name, convert their input into BO
         */
        Map<ABIDefinition,TypeSpec> result = new HashMap<>();
        for(Map.Entry<String, List<ABIDefinition>> e: functions.entrySet()){
            List<ABIDefinition> definitions = e.getValue();
            for(int i=0;i<definitions.size();i++){
                ABIDefinition abiDef = definitions.get(i);
                String functionName =  CommonUtil.makeFirstCharUpperCase(abiDef.getName());
                String overloadMark = i>0?Integer.toString(i):"";
                String className = contractName
                        + functionName
                        + overloadMark
                        + INPUT;
                TypeSpec inputType = buildBOType(className, abiDef.getInputs());
                result.put(abiDef, inputType);
            }
        }
        return result;
    }

    private TypeSpec buildBOType(String className, List<ABIDefinition.NamedType> args){
        /**
         * 1. Check: No need to generate BO for functions with no args
         */
        if(args.isEmpty()) return null;//No need

        /**
         * 2. BO Type
         */
        // metadata
        TypeSpec.Builder boBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(AllArgsConstructor.class);
        // Fields
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

        // Methods
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
}
