package com.webank.scaffold.artifact.webase;

import com.webank.scaffold.artifact.DirectoryArtifact;
import com.webank.scaffold.artifact.MainJavaDir;
import com.webank.scaffold.artifact.webase.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.config.GeneratorOptions;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constant.CompileConstants;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.IOUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.codegen.SolidityContractWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author marsli
 * @Description
 * @data 2021/01/19
 */
@Getter
public class NewMainDir extends DirectoryArtifact {
    private static final Logger logger = LoggerFactory.getLogger(NewMainDir.class);

    private static final String MAIN_DIR = "main";
    private static final String SOL_Dir = "contracts";
    
    private List<ContractInfo> contractInfoList;
    private String systemPeers;
    private Integer groupId;
    private String hexPrivateKey;
    private Map<String, String> sdkContentMap;

    private UserConfig config;

    public NewMainDir(File basePath, UserConfig config, List<ContractInfo> contractInfoList) {
        super(basePath);
        this.config = config;
        this.contractInfoList = contractInfoList;
    }

    public NewMainDir(File basePath, UserConfig config, List<ContractInfo> contractInfoList,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap) {
        super(basePath);
        this.config = config;
        this.contractInfoList = contractInfoList;
        this.systemPeers = systemPeers;
        this.groupId = groupId;
        this.hexPrivateKey = hexPrivateKey;
        this.sdkContentMap = sdkContentMap;
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
        NewMainResourceDir resources = new NewMainResourceDir(this.toFile(), contractInfoList,
            systemPeers, groupId, hexPrivateKey, sdkContentMap);
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

        /**
         * 4. generate raw solidity java file
         * generate raw java file of soldiity: 0.4.25
         */
        this.generateSolJavaFiles();

    }

    @Override
    public String getName() {
        return MAIN_DIR;
    }

    private File generateContractsDir() throws IOException {
        if (contractInfoList == null || contractInfoList.isEmpty()) {
            throw new ScaffoldException("contractInfoList is empty!");
        }
        File contractsDir = new File(this.toFile(), SOL_Dir);
        for (ContractInfo info : contractInfoList) {
            IOUtil.writeStringToFile(info.getSolRawString(), contractsDir,
                info.getContractName() + CompileConstants.SOL_FILE_SUFFIX);
        }
        return contractsDir;
    }

    private void generateSolJavaFiles() {
        String groupName = config.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifactName = config.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        String javaFileOutputDir = this.getParentDir().getPath() + File.separator + MAIN_DIR
            + File.separator + "java" + File.separator + groupName.replace(".", File.separator)
            + File.separator + artifactName + File.separator + "raw";
        logger.info("generateSolJavaFiles javaFileOutputDir:{}", javaFileOutputDir);
        File check = new File(javaFileOutputDir);
        if (!check.exists()) {
            boolean result = check.mkdirs();
            logger.info("javaFileOutputDir mkdir:{}", result);
        }
        for (ContractInfo info : contractInfoList) {
            System.out.println("now generate java file of " + info.getContractName());
            logger.info("now generate java file of {}", info.getContractName());
            try {
                new SolidityContractWrapper().generateJavaFiles(
                        info.getContractName(),
                        info.getBinStr(),
                        info.getSmBinStr(),
                        info.getAbiStr(),
                        javaFileOutputDir,
                        "");
            } catch (Exception e) {
                System.out.println("generate java file error" + e.getMessage());
                logger.error("generate java file of {} failed for error:{}", info.getContractName(), e);
            }
        }

    }
}
