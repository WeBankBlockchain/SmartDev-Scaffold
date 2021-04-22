package com.webank.scaffold.exception;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class ScaffoldException extends RuntimeException{

    /** @Fields serialVersionUID : TODO */
    private static final long serialVersionUID = -4165654620608938012L;

    public ScaffoldException(Exception error){
        super(error);
    }

    public ScaffoldException(String message){
        super(message);
    }

}
