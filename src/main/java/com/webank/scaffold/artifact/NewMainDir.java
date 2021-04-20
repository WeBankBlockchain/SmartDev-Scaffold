package com.webank.scaffold.artifact;

import com.webank.scaffold.artifact.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constant.CompileConstants;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.IOUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author marsli
 * @Description
 * @data 2021/01/19
 */
public class NewMainDir extends DirectoryArtifact {
    private static final String MAIN_DIR = "main";
    private static final String SOL_Dir = "contracts";
    
    List<ContractInfo> contractInfoList;
    List<SolInfo> solList;
    @Getter
    @Setter
    public static class SolInfo {
        private String solRawString;
        private String contractName;
    }

    private UserConfig config;

    public NewMainDir(File basePath, UserConfig config, List<SolInfo> solList, List<ContractInfo> contractInfoList) {
        super(basePath);
        this.config = config;
        this.solList = solList;
        this.contractInfoList = contractInfoList;
    }

    /**
     * Generate sub directories: contracts，abi，bin，smbin，java
     * @throws Exception
     */
    @Override
    protected void doGenerateSubContents() throws Exception {
        /**
         * 1. Write raw solidity contracts to "contracts" directory
         */
        File contractsDir = generateContractsDir();

        /**
         * 2. Resources, set contractInfoList instead of get from files
         */
        NewMainResourceDir resources = new NewMainResourceDir(this.toFile(), contractInfoList);
        resources.generate();

        /**
         * 3. Javas, remain unchanged
         */
        //1. Abi And Bin
        String need = StringUtils.join(
            contractInfoList.stream().map(ContractInfo::getContractName).toArray(),",");
        MainJavaDir javas = new MainJavaDir(
                this.toFile(),
                resources.getAbiDir(),
                need,
                this.config);
        javas.generate();
    }

    @Override
    public String getName() {
        return MAIN_DIR;
    }

    private File generateContractsDir() throws IOException {
        if (solList == null || solList.isEmpty()) {
            throw new ScaffoldException("solList is empty!");
        }
        File contractsDir = new File(this.toFile(), SOL_Dir);
        for (SolInfo info : solList) {
            IOUtil.writeStringToFile(info.getSolRawString(), contractsDir,
                info.contractName + CompileConstants.SOL_FILE_SUFFIX);
        }
        return contractsDir;
    }
}
