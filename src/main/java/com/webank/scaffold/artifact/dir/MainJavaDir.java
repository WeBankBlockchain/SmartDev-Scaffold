package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.artifact.file.ApplicationJava;
import com.webank.scaffold.artifact.file.CommonResponseJava;
import com.webank.scaffold.artifact.file.IOUtilJava;
import com.webank.scaffold.artifact.file.SdkBeanConfigJava;
import com.webank.scaffold.builder.*;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.handler.ServicesHandler;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.ABIUtil;
import com.webank.scaffold.util.IOUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/03/02
 */
public class MainJavaDir extends DirectoryArtifact {

    private File abiDir;

    private ServicesHandler srvBuilder;
    private UserConfig config;

    public MainJavaDir(File parentDir, File abi, UserConfig config) {
        super(parentDir);
        this.abiDir = abi;
        this.srvBuilder = new ServicesHandler(config);
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        //1. IOUtil
        handleIOUtil();
        //2. Bo and service
        handleBOAndService();
        //3. Application
        handleApplication();
        //4. System Config
        handleSystemConfig();
        //5. Contract config
        handleContractConfig();
        //6. Sdk Bean config
        handleSdkBeanConfig();
        //7. Common response
        handleCommonResponse();
    }

    private void handleApplication() throws Exception{
        String pkg = config.getGroup() + "." + config.getArtifact();
        ApplicationJava applicationJava = new ApplicationJava(IOUtil.convertPackageToFile(this.toFile(),pkg), config);
        applicationJava.generate();
    }

    private void handleIOUtil() throws Exception{
        String utilsPackage = config.getGroup() + "." + config.getArtifact() + FileNameConstants.UTILS_PKG_POSTFIX;
        IOUtilJava ioUtilJava = new IOUtilJava(IOUtil.convertPackageToFile(this.toFile(),utilsPackage), config);
        ioUtilJava.generate();
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
