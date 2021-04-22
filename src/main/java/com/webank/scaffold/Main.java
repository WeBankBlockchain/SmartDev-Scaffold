package com.webank.scaffold;

import com.webank.scaffold.cmd.ScaffoldRunner;
import picocli.CommandLine;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class Main {

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new ScaffoldRunner());
        cmd.execute(args);
    }
}
