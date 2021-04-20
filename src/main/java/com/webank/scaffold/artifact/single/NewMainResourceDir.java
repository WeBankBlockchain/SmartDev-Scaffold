package com.webank.scaffold.artifact.single;

import com.webank.scaffold.ContractCompiler;
import com.webank.scaffold.artifact.ApplicationProperties;
import com.webank.scaffold.artifact.ConfDir;
import com.webank.scaffold.artifact.DirectoryArtifact;
import com.webank.scaffold.constant.CompileConstants;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.CommonUtil;
import com.webank.scaffold.util.IOUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.transaction.model.CommonConstant;

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
    @Getter
    @Setter
    public static class ContractInfo {
        private String abiStr;
        private String binStr;
        private String smBinStr;
        private String contractName;
    }

    public NewMainResourceDir(File parentDir, List<ContractInfo> contractInfoList) {
        super(parentDir);
        this.contractInfoList = contractInfoList;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        if (contractInfoList == null || contractInfoList.isEmpty()) {
            throw new ScaffoldException("contractInfoList is empty!");
        }
        //1. Abi And Bin
        List<String> contractNameList = contractInfoList.stream().map(ContractInfo::getContractName).collect(
            Collectors.toList());
        this.generateAbiBinFile();
        //2. Conf
        ConfDir confDir = new ConfDir(this.toFile());
        confDir.generate();
        //3. Application.properties

        ApplicationProperties applicationProperties = new ApplicationProperties(this.toFile(), contractNameList);
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
//                + info.contractName + CompileConstants.ABI_FILE_SUFFIX;
            File binFile = new File(outputBase.getPath() + File.separator + CompileConstants.BIN_DIR);
//                + info.contractName + CompileConstants.BIN_FILE_SUFFIX;
            File smBinFile = new File(outputBase.getPath() + File.separator + CompileConstants.SMBIN_DIR);
//                + info.contractName + CompileConstants.BIN_FILE_SUFFIX;
            // write to file
            IOUtil.writeStringToFile(info.abiStr, abiFile, info.contractName + CompileConstants.ABI_FILE_SUFFIX);
            IOUtil.writeStringToFile(info.binStr, binFile, info.contractName + CompileConstants.BIN_FILE_SUFFIX);
            IOUtil.writeStringToFile(info.smBinStr, smBinFile, info.contractName + CompileConstants.BIN_FILE_SUFFIX);
        }
    }

    @Override
    public String getName() {
        return "resources";
    }

}
