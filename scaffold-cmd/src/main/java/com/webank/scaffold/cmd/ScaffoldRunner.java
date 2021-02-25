package com.webank.scaffold.cmd;


import com.webank.scaffold.core.factory.ProjectFactory;
import picocli.CommandLine;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */

@CommandLine.Command(name = "ScaffoldRunner")
public class ScaffoldRunner implements Runnable{
    @CommandLine.Option(names = {"-s", "--sol"}, required = true, description = "Required. Solidity contracts dir.")
    private String solidityDir;

    @CommandLine.Option(names = {"-g", "--group"}, required = true,defaultValue = "org.example",description = "Optional. Group name.")
    private String group;

    @CommandLine.Option(names = {"-a", "--artifact"}, required = true, defaultValue = "demo",description = "Optional. Artifact name.")
    private String artifact;

    @CommandLine.Option(names = {"-o", "--output"},required = true, defaultValue = "artifacts",description = "Optional. Output directory.")
    private String output;

    @CommandLine.Option(names = {"-f", "--filter"}, required = false,defaultValue = "*",description = "Optional. Contract filter, you can use wild char")
    private String filter;

    @Override
    public void run() {
        new ProjectFactory().buildProjectDir(solidityDir, group, artifact, output, filter);
    }

}
