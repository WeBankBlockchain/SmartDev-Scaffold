package com.webank.scaffold.handler;

import com.squareup.javapoet.TypeName;
import org.fisco.bcos.sdk.v3.codec.wrapper.ABIDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
public class SolidityTypeHandler {


    /**
     * Convert a strong typed solidity type.
     * uint256 -> Uint256.class
     * uint256[] -> DynamicArray[Uint256].class
     * uint256[2] ->StaticArray2[Uint256].class
     * uint256[][2] ->DynamicArray[StaticArray2[Uint256]].class
     * @param namedType
     * @return
     */
    public static TypeName convert(ABIDefinition.NamedType namedType){
        ContractWrapperHelper handler = ContractWrapperHelper.INSTANCE;

        try{
            //如果是结构体，那么返回结构体对应类型的名字，比如xxx.xxx.xxx.Entry
            if (!namedType.getComponents().isEmpty()){

                List<ABIDefinition.NamedType> list = new ArrayList<>();
                list.add(namedType);
                TypeName typeName = handler.buildTypeNames(list).get(0);
//                System.out.println("aaa"+ typeName);
                return typeName;
            }
            TypeName solType = handler.buildTypeName(namedType.getType());
            TypeName nativeType = handler.getNativeType(solType);
            return nativeType;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
