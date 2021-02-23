package com.webank.scaffold.cmd;

import picocli.CommandLine;

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