package com.webank.scaffold.artifact.webase;

import com.webank.scaffold.ContractCompiler;
import com.webank.scaffold.artifact.DirectoryArtifact;
import com.webank.scaffold.constant.CompileConstants;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.IOUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.fisco.bcos.sdk.codegen.SolidityContractGenerator;

/**
 *
 * @author marsli
 * @Description
 * @data 2021/03/02
 */
@Getter
public class NewMainResourceDir extends DirectoryArtifact {
    private File abiDir;
    private File binDir;
    private File smBinDir;

    List<ContractInfo> contractInfoList;
    private String systemPeers;
    private Integer groupId;
    private String hexPrivateKey;
    private Map<String, String> sdkContentMap;
    @Getter
    @Setter
    public static class ContractInfo {
        private String abiStr;
        private String binStr;
        private String smBinStr;
        private String contractName;
        // can be blank below
        private String contractAddress;
        private String solRawString;
    }

    public NewMainResourceDir(File parentDir, List<ContractInfo> contractInfoList) {
        super(parentDir);
        this.contractInfoList = contractInfoList;
    }

    public NewMainResourceDir(File parentDir, List<ContractInfo> contractInfoList,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap) {
        super(parentDir);
        this.contractInfoList = contractInfoList;
        this.systemPeers = systemPeers;
        this.groupId = groupId;
        this.hexPrivateKey = hexPrivateKey;
        this.sdkContentMap = sdkContentMap;
    }

    public NewMainResourceDir(File parentDir, List<ContractInfo> contractInfoList,
        String systemPeers, Integer groupId, String hexPrivateKey) {
        super(parentDir);
        this.contractInfoList = contractInfoList;
        this.systemPeers = systemPeers;
        this.groupId = groupId;
        this.hexPrivateKey = hexPrivateKey;
    }


    @Override
    protected void doGenerateSubContents() throws Exception {
        if (contractInfoList == null || contractInfoList.isEmpty()) {
            throw new ScaffoldException("contractInfoList is empty!");
        }
        //1. Abi And Bin
        this.generateAbiBinFile();
        //2. Conf
        NewConfDir confDir = new NewConfDir(this.toFile(), sdkContentMap);
        confDir.generate();
        //3. Application.properties
        NewApplicationProperties applicationProperties = new NewApplicationProperties(this.toFile(),
            this.contractInfoList, this.systemPeers, this.groupId, this.hexPrivateKey);
        applicationProperties.generate();
    }

    private void generateAbiBinFile() throws IOException {
        if (contractInfoList == null || contractInfoList.isEmpty()) {
            throw new ScaffoldException("contractInfoList is empty!");
        }
        File outputBase = this.toFile();
        this.abiDir  = new File(outputBase, CompileConstants.ABI_DIR);
        this.binDir = new File(outputBase, CompileConstants.BIN_DIR);
        this.smBinDir = new File(outputBase, CompileConstants.SMBIN_DIR);
        for (ContractInfo info : this.contractInfoList) {
            // get file path, ex: resources/abi/HelloWorld.abi, resources/bin/ecc/HelloWorld.bin
            File abiFile = new File(outputBase.getPath() + File.separator + CompileConstants.ABI_DIR);
            File binFile = new File(outputBase.getPath() + File.separator + CompileConstants.BIN_DIR);
            File smBinFile = new File(outputBase.getPath() + File.separator + CompileConstants.SMBIN_DIR);
            // write to file
            IOUtil.writeStringToFile(info.abiStr, abiFile, info.contractName + CompileConstants.ABI_FILE_SUFFIX);
            IOUtil.writeStringToFile(info.binStr, binFile, info.contractName + CompileConstants.BIN_FILE_SUFFIX);
            IOUtil.writeStringToFile(info.smBinStr, smBinFile, info.contractName + CompileConstants.BIN_FILE_SUFFIX);
        }
    }


//    private void generateContractJava() {
//        for (ContractInfo contractInfo : contractInfoList) {
//            SolidityContractGenerator generator = new SolidityContractGenerator()
//        }
//    }


    private void compileContract(File contractDir) throws Exception{
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
