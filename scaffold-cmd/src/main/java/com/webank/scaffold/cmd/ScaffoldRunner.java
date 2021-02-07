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

    @CommandLine.Option(names = {"-f", "--filter"}, required = false,defaultValue = "*",description = "Contract filter, you can use wild char")
    private String filter;

    @CommandLine.Option(names = {"-p", "--peers"}, required = true, description = "Peers to connect, split by comma")
    private String peers;

    @CommandLine.Option(names = {"-s", "--sol"}, required = true, description = "Solidity contracts dir")
    private String solidityDir;

    @CommandLine.Option(names = {"-c", "--cert"}, required = true, description = "Ca cert directory")
    private String certDir;


    @CommandLine.Option(names = {"-o", "--output"},required = true,description = "Output directory")
    private String output;

    @Override
    public void run() {
        new ProjectFactory().buildProjectDir("org.example", "demo",filter, peers,
                solidityDir, certDir, output);
    }

}
