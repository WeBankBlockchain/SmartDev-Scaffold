package com.webank.scaffold.compiler;

import com.webank.scaffold.constants.DirNameConstants;
import com.webank.solc.plugin.compiler.CompileSolToJava;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class ContractCompiler {

    private File output;
    private File solDir;

    public ContractCompiler(File output, File solDir){
        this.output = output;
        this.solDir = solDir;
    }

    /**
     * Compile solidity and generates:abi、bin、smbin.
     * @throws Exception
     */
    public void compile() throws Exception{

        File abiDir = new File(this.output, DirNameConstants.ABI_DIR);
        File binDir = new File(this.output, DirNameConstants.BIN_DIR);
        File smBinDir = new File(this.output, DirNameConstants.SMBIN_DIR);

        CompileSolToJava compiler = new CompileSolToJava();
        String solName = "*";//Compile everything

        compiler.compileSolToJava(
                solName,
                null,
                solDir,
                abiDir,
                binDir,
                smBinDir,
                null
                );
    }

}
