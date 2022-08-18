package com.webank.scaffold.artifact.dir;

import java.io.File;
import java.util.List;

import com.webank.scaffold.artifact.file.BcosConfigJava;
import com.webank.scaffold.builder.*;
import org.apache.commons.io.FileUtils;

import com.webank.scaffold.artifact.file.ApplicationJava;
import com.webank.scaffold.artifact.file.CommonResponseJava;
import com.webank.scaffold.artifact.file.SdkBeanConfigJava;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.util.ABIUtil;
import com.webank.scaffold.util.IOUtil;

/**
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/03/02
 */
public class MainJavaDir extends DirectoryArtifact {

    private File abiDir;
    private UserConfig config;

    public MainJavaDir(File parentDir, File abi, UserConfig config) {
        super(parentDir);
        this.abiDir = abi;
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        handleUtil();
        handleSystemConfig();
        handleBcosConfig();
        handleContractConfig();
        handleContractConstants();
        handleBOAndService();
        handleApplication();
        handleSdkBeanConfig();
        handleCommonResponse();
    }

    private void handleContractConstants() throws Exception{
        List<String> contractList = ABIUtil.contracts(abiDir, this.config.getNeed());
        ContractConstantsBuilder constantsBuilder = new ContractConstantsBuilder(this.config, contractList);
        File javaDir = this.toFile();
        constantsBuilder.generateJavaFile(FileNameConstants.CONSTANT_PKG_POSTFIX, javaDir);
    }

    private void handleApplication() throws Exception{
        String pkg = config.getGroup() + "." + config.getArtifact();
        ApplicationJava applicationJava = new ApplicationJava(IOUtil.convertPackageToFile(this.toFile(),pkg), config);
        applicationJava.generate();
    }

    private void handleUtil() throws Exception{
        //hook
    }

    private void handleBOAndService() throws Exception{
        File javaDir = this.toFile();
        List<String> contractList = ABIUtil.contracts(abiDir, this.config.getNeed());
        for(String contractName :contractList){
            File abiFile = new File(abiDir, contractName + FileNameConstants.ABI_FILE_POSTFIX);
            String abiStr = FileUtils.readFileToString(abiFile);

            // create constructor bo
            ConstructorBoFileBuilder cbBuilder = new ConstructorBoFileBuilder(contractName, abiStr, this.config);
            cbBuilder.generateJavaFile(FileNameConstants.BO_PKG_POSTFIX, javaDir);

            // create struct bo
            StructsFileBuilder structsBuilder = new StructsFileBuilder(abiStr, this.config);
            structsBuilder.generateJavaFile(FileNameConstants.BO_PKG_POSTFIX, javaDir);

            // create function bo
            FunctionBoFileBuilder fbBuilder = new FunctionBoFileBuilder(contractName, abiStr, this.config);
            fbBuilder.generateJavaFile(FileNameConstants.BO_PKG_POSTFIX, javaDir);

            //Build service
            ServiceFileBuilder sfBuilder = new ServiceFileBuilder(contractName, abiStr, this.config);
            sfBuilder.generateJavaFile(FileNameConstants.SERVICE_PKG_POSTFIX, javaDir);
        }
    }

    private void handleSystemConfig() throws Exception {
        SystemConfigBuilder systemConfigBuilder = new SystemConfigBuilder(config);
        systemConfigBuilder.generateJavaFile(FileNameConstants.CONFIG_PKG_POSTFIX, this.toFile());
    }

    private void handleBcosConfig() throws Exception {
        String configPackage = config.getGroup() + "." + config.getArtifact() + FileNameConstants.CONFIG_PKG_POSTFIX;;
        BcosConfigJava bcosConfigJava = new BcosConfigJava(IOUtil.convertPackageToFile(this.toFile(),configPackage), config);
        bcosConfigJava.generate();
    }

    private void handleContractConfig() throws Exception {
        List<String> contracts = ABIUtil.contracts(this.abiDir, this.config.getNeed());
        ContractConfigBuilder contractConfigBuilder = new ContractConfigBuilder(config, contracts);
        contractConfigBuilder.generateJavaFile(FileNameConstants.CONFIG_PKG_POSTFIX, this.toFile());
    }

    private void handleSdkBeanConfig() throws Exception{
        String configPackage = config.getGroup() + "." + config.getArtifact() + FileNameConstants.CONFIG_PKG_POSTFIX;;
        SdkBeanConfigJava sdkBeanConfigJava = new SdkBeanConfigJava(IOUtil.convertPackageToFile(this.toFile(),configPackage), config);
        sdkBeanConfigJava.generate();
    }

    private void handleCommonResponse()  throws Exception{
        String utilsPackage = config.getGroup() + "." + config.getArtifact() + FileNameConstants.MODEL_FILE_POSTFIX;;
        CommonResponseJava commonResponseJava = new CommonResponseJava(IOUtil.convertPackageToFile(this.toFile(),utilsPackage), config);
        commonResponseJava.generate();
    }

    @Override
    public String getName() {
        return DirNameConstants.JAVA_DIR;
    }
}
