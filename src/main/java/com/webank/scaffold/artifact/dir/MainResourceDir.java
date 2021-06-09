package com.webank.scaffold.artifact.dir;

import java.io.File;

import com.webank.scaffold.artifact.file.ApplicationProperties;
import com.webank.scaffold.compiler.ContractCompiler;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.util.ABIUtil;

/**
 *
 * @author aaronchu
 * @Description
 * @data 2021/03/02
 */
public class MainResourceDir extends DirectoryArtifact {

    private UserConfig config;
    private File abiDir;

    public MainResourceDir(File parentDir, UserConfig config) {
        super(parentDir);
        this.config = config;
        this.abiDir = new File(this.toFile(), DirNameConstants.ABI_DIR);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {

        //1. create Abi And Bin
        ContractCompiler compiler = new ContractCompiler(this.toFile(), new File(this.config.getSolidityDir()));
        compiler.compile();

        //2. create conf file
        ConfDir confDir = new ConfDir(this.toFile());
        confDir.generate();

        //3. create Application.properties
        ApplicationProperties applicationProperties = new ApplicationProperties(
                this.toFile(),
                ABIUtil.contracts(new File(this.toFile(), DirNameConstants.ABI_DIR), this.config.getNeed()));
        applicationProperties.generate();
    }

    @Override
    public String getName() {
        return DirNameConstants.RESOURCE;
    }

    /**
     * @return the config
     */
    public UserConfig getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(UserConfig config) {
        this.config = config;
    }

    /**
     * @return the abiDir
     */
    public File getAbiDir() {
        return abiDir;
    }

    /**
     * @param abiDir the abiDir to set
     */
    public void setAbiDir(File abiDir) {
        this.abiDir = abiDir;
    }
}
