package com.webank.scaffold.core.artifact;

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.core.clhandler.BOHandler;
import com.webank.scaffold.core.clhandler.ServiceBeanConfigHandler;
import com.webank.scaffold.core.clhandler.ServicesHandler;
import com.webank.scaffold.core.clhandler.SystemConfigHandler;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.CommonUtil;
import com.webank.scaffold.core.util.IOUtil;
import com.webank.scaffold.core.util.PackageNameUtil;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/02
 */
public class MainJavaDir extends DirectoryArtifact {

    private File abiDir;
    private String need;

    private BOHandler bOHandler;
    private ServicesHandler srvBuilder;
    private UserConfig config;

    public MainJavaDir(File parentDir, File abi, String need, UserConfig config) {
        super(parentDir);
        this.abiDir = abi;
        this.need = need;
        this.bOHandler = new BOHandler(config);
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
        //5. Sdk Bean config
        handleSdkBeanConfig();
        //6. Service Bean config
       // handleServiceBeanConfig();
    }


    private void handleApplication() throws Exception{
        File javaDir = this.toFile();
        String pkg = PackageNameUtil.getRootPackageName(this.config);
        ApplicationJava applicationJava = new ApplicationJava(PackageNameUtil.convertPackageToFile(javaDir,pkg), config);
        applicationJava.generate();
    }

    private void handleIOUtil() throws Exception{
        File javaDir = this.toFile();
        String utilsPackage = PackageNameUtil.getUtilsPackageName(config);
        IOUtilJava ioUtilJava = new IOUtilJava(PackageNameUtil.convertPackageToFile(javaDir,utilsPackage), config);
        ioUtilJava.generate();
    }

    private void handleBOAndService() throws Exception{
        File javaDir = this.toFile();
        List<String> contractList = CommonUtil.contracts(abiDir, need);
        for(String contractName :contractList){
            File abiFile = new File(abiDir, contractName+".abi");
            String abiStr = IOUtil.readAsString(abiFile);
            //Build BO
            TypeSpec ctorBO = this.bOHandler.buildCtorBO(contractName, abiStr);
            Map<ABIDefinition, TypeSpec> functionBos = this.bOHandler.buildFunctionBO(contractName, abiStr);
            this.bOHandler.exportBOs(javaDir, Arrays.asList(ctorBO));
            this.bOHandler.exportBOs(javaDir, functionBos.values().stream().collect(Collectors.toList()));
            //Build service
            TypeSpec serviceType = srvBuilder.build(contractName, ctorBO, functionBos);
            this.srvBuilder.exportBO(serviceType, javaDir);
        }
    }


    private void handleSystemConfig() throws Exception {
        List<String> contracts = CommonUtil.contracts(this.abiDir, this.need);
        SystemConfigHandler configBuilder = new SystemConfigHandler(this.toFile(), contracts, config);
        configBuilder.export();
    }

    private void handleSdkBeanConfig() throws Exception{
        File javaDir = this.toFile();
        String configPackage = PackageNameUtil.getConfigPackageName(config);
        SdkBeanConfigJava sdkBeanConfigJava = new SdkBeanConfigJava(PackageNameUtil.convertPackageToFile(javaDir,configPackage), config);
        sdkBeanConfigJava.generate();
    }

    private void handleServiceBeanConfig()  throws Exception{
        List<String> contracts = CommonUtil.contracts(this.abiDir, this.need);
        ServiceBeanConfigHandler configBuilder = new ServiceBeanConfigHandler(this.toFile(), contracts, config);
        configBuilder.export();
    }

    @Override
    public String getName() {
        return "java";
    }
}
