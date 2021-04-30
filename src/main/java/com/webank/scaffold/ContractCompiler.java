package com.webank.scaffold;

import com.webank.solc.plugin.compiler.CompileSolToJava;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class ContractCompiler {

    private File solDir;

    public ContractCompiler(File solDir){
        this.solDir = solDir;
    }

    /**
     * Compile solidity and generates:
     * abi、bin、smbin、javacontract.
     * @throws Exception
     */
    public void compile(File abiOut, File binOut, File smBinOut) throws Exception {
        CompileSolToJava compiler = new CompileSolToJava();
        String solName = "*";//Compile everything

        compiler.compileSolToJava(
                solName,
                null,
                solDir,
                abiOut,
                binOut,
                smBinOut,
                null
                );
    }

}
