package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class MainDir extends DirectoryArtifact {

    private UserConfig config;

    public MainDir(File parentPath, UserConfig config) {
        super(parentPath);
        this.config = config;
    }

    /**
     * Generate sub directories: contracts，abi，bin，smbin，java
     * @throws Exception
     */
    @Override
    protected void doGenerateSubContents() throws Exception {

        // process contracts dir
        ContractsDir contractsDir = new ContractsDir(this.toFile(), config);
        contractsDir.generate();

        // process resource dir
        MainResourceDir resources = new MainResourceDir(this.toFile(), config);
        resources.generate();

        // process java code dir
        MainJavaDir javas = new MainJavaDir(this.toFile(), resources.getAbiDir(), this.config);
        javas.generate();
    }

    @Override
    public String getName() {
        return DirNameConstants.MAIN_DIR;
    }



}
