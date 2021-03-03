package com.webank.scaffold.core.builder;

import lombok.Data;

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
