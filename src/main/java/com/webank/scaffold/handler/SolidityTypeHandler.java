package com.webank.scaffold.handler;

import com.squareup.javapoet.TypeName;
import org.fisco.bcos.sdk.codegen.SolidityContractWrapper;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
public class SolidityTypeHandler extends SolidityContractWrapper {

    /**
     * Convert a strong typed solidity type.
     * uint256 -> Uint256.class
     * uint256[] -> DynamicArray[Uint256].class
     * uint256[2] ->StaticArray2[Uint256].class
     * uint256[][2] ->DynamicArray[StaticArray2[Uint256]].class
     * @param typeName
     * @return
     */
    public static TypeName convert(String typeName){
        TypeName solType = SolidityContractWrapper.buildTypeName(typeName);
        TypeName nativeType = SolidityContractWrapper.getNativeType(solType);
        return nativeType;
    }
}
