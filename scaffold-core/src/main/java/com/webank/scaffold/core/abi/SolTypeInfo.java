package com.webank.scaffold.core.abi;

import lombok.Data;
import org.fisco.bcos.sdk.abi.datatypes.generated.AbiTypes;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
@Data
public class SolTypeInfo {

    private String typeString;

    private Class primaryClazz;

    private int arrayNests;


}
