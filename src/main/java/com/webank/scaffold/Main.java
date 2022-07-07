package com.webank.scaffold;

import com.squareup.javapoet.TypeName;
import com.webank.scaffold.cmd.ScaffoldRunner;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.handler.SolidityTypeHandler;
import org.fisco.bcos.sdk.v3.codegen.ContractWrapper;
import picocli.CommandLine;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class Main {

    public static void main(String[] args) throws Exception{
        CommandLine cmd = new CommandLine(new ScaffoldRunner());
        cmd.execute(args);
    }
}

