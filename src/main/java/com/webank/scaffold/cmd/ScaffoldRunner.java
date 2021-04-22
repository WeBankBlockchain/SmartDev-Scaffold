package com.webank.scaffold.cmd;


import com.webank.scaffold.factory.ProjectFactory;
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

    @CommandLine.Option(names = {"-n", "--need"}, required = false,defaultValue = "",description = "Optional. The contracts you need,for example Contract1,Contract2,Contract3")
    private String need;

    @Override
    public void run() {
        ProjectFactory factory = new ProjectFactory(group, artifact, solidityDir,  output, need);
        factory.createProject();
    }

}
