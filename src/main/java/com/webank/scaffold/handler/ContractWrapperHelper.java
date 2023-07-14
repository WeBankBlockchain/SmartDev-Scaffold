package com.webank.scaffold.handler;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import com.webank.scaffold.util.ReflectUtil;
import org.fisco.bcos.codegen.v3.wrapper.ContractWrapper;
import org.fisco.bcos.sdk.v3.codec.wrapper.ABIDefinition;

/** Generate Java Classes based on generated Solidity bin and abi files. */
public class ContractWrapperHelper extends ContractWrapper {

    public static ContractWrapperHelper INSTANCE = new ContractWrapperHelper(false);
    ContractWrapperHelper(boolean isWasm) {
        super(isWasm);
    }

    public List<TypeName> buildTypeNames(List<ABIDefinition.NamedType> namedTypes) throws ClassNotFoundException {
        return super.buildTypeNames(namedTypes);
    }

    public static TypeName buildTypeName(String typeDeclaration) throws ClassNotFoundException {
        return ContractWrapper.buildTypeName(typeDeclaration);
    }

    public static TypeName getNativeType(TypeName typeName) {
        return ContractWrapper.getNativeType(typeName);
    }

    public List<TypeSpec> buildStructTypes(List<ABIDefinition> functionDefinitions){
        Class[] paramsClasses = new Class[]{List.class};
        Object[] params = new Object[]{functionDefinitions};

        return (List<TypeSpec>)ReflectUtil.invokeSuper(this, "buildStructTypes", paramsClasses, params);
    }
}
