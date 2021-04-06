package com.webank.scaffold.exception;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class ScaffoldException extends RuntimeException{

    public ScaffoldException(Exception error){
        super(error);
    }

    public ScaffoldException(String message){
        super(message);
    }

}
