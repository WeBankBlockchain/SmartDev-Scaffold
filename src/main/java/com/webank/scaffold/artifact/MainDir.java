package com.webank.scaffold.artifact;

import com.webank.scaffold.DirectoryCopier;
import com.webank.scaffold.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class MainDir extends DirectoryArtifact {
    private static final String MAIN_DIR = "main";
    private static final String SOL_Dir = "contracts";


    private File srcSolDir;
    private UserConfig config;
    private String need;

    public MainDir(String need, File basePath, File srcContractDir, UserConfig config) {
        super(basePath);
        this.srcSolDir = srcContractDir;
        this.need = need;
        this.config = config;

    }

    /**
     * Generate sub directories: contracts，abi，bin，smbin，java
     * @throws Exception
     */
    @Override
    protected void doGenerateSubContents() throws Exception {
        /**
         * 1. Copy raw solidity contracts to "contracts" directory
         */
        File contractsDir = generateContractsDir();

        /**
         * 2. Resources
         */
        MainResourceDir resources = new MainResourceDir(this.toFile(), contractsDir, this.need);
        resources.generate();

        /**
         * 3. Javas
         */
        MainJavaDir javas = new MainJavaDir(
                this.toFile(),
                resources.getAbiDir(),
                this.need,
                this.config);
        javas.generate();
    }

    @Override
    public String getName() {
        return MAIN_DIR;
    }

    private File generateContractsDir() throws Exception {
        File contractsDir = new File(this.toFile(), SOL_Dir);
        DirectoryCopier copier = new DirectoryCopier();
        copier.copy(this.srcSolDir.getAbsolutePath(),contractsDir.getAbsolutePath());
        return contractsDir;
    }
}
