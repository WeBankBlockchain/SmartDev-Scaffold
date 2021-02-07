package com.webank.scaffold.core;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.PackageNameUtil;
import com.webank.solc.plugin.compiler.CompileSolToJava;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class ContractCompiler {

    private File solDir;
    private File outputBaseDir;
    private UserConfig config;

    public ContractCompiler(File solDir, File outputBaseDir, UserConfig config){
        this.solDir = solDir;
        this.outputBaseDir = outputBaseDir;
        this.config = config;
    }

    /**
     * Compile solidity and generates:
     * abi、bin、smbin、javacontract.
     * @throws Exception
     */
    public void compile() throws Exception{
        CompileSolToJava compiler = new CompileSolToJava();
        String solName = "*";//Compile everything

        String group = this.config.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifact =  this.config.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        String packageName = PackageNameUtil.getRootPackageName(group, artifact) + ".contracts";

        compiler.compileSolToJava(
                solName,
                packageName,
                solDir,
                new File(outputBaseDir, "abi"),
                new File(outputBaseDir, "bin/ecc"),
                new File(outputBaseDir, "bin/sm"),
                new File(outputBaseDir, "java")
                );
    }

}
