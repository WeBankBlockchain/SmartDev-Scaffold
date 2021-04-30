package com.webank.scaffold.artifact;

import com.webank.scaffold.ContractCompiler;
import com.webank.scaffold.constant.CompileConstants;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.CommonUtil;
import lombok.Getter;

import java.io.File;

/**
 *
 * @author aaronchu
 * @Description
 * @data 2021/03/02
 */
@Getter
public class MainResourceDir extends DirectoryArtifact {

    private File contractsDir;
    private String need;

    private File abiDir;
    private File binDir;
    private File smBinDir;

    public MainResourceDir(File parentDir, File contractsDir, String need) {
        super(parentDir);
        this.contractsDir = contractsDir;
        this.need = need;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        //1. Abi And Bin
        this.compileContract(contractsDir);
        //2. Conf
        ConfDir confDir = new ConfDir(this.toFile());
        confDir.generate();
        //3. Application.properties
        ApplicationProperties applicationProperties = new ApplicationProperties(this.toFile(), CommonUtil.contracts(this.abiDir, need));
        applicationProperties.generate();
    }

    private void compileContract(File contractDir) throws Exception {
        ContractCompiler compiler = new ContractCompiler(contractDir);
        try{
            File outputBase = this.toFile();
            this.abiDir  = new File(outputBase, CompileConstants.ABI_DIR);
            this.binDir = new File(outputBase, CompileConstants.BIN_DIR);
            this.smBinDir = new File(outputBase, CompileConstants.SMBIN_DIR);
            compiler.compile(abiDir, binDir, smBinDir);
        }
        catch (Exception ex){
            throw new ScaffoldException(ex);
        }
    }

    @Override
    public String getName() {
        return "resources";
    }

}
