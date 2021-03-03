package com.webank.scaffold.core.artifact;

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.core.util.CommonUtil;
import com.webank.scaffold.core.util.IOUtil;
import org.apache.commons.io.FilenameUtils;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/02
 */
public class ApplicationProperties implements Artifact {

    private File parentDir;
    private List<String> contracts;

    public ApplicationProperties(File parentDir, List<String> contracts){
        this.parentDir = parentDir;
        this.contracts = contracts;
    }

    @Override
    public void generate() throws Exception {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.toFile()))){
            writeDefaultKeys(writer);
            writeContractKeys(writer);
            writer.flush();
        }
    }
    private void writeDefaultKeys(BufferedWriter writer) throws IOException {
        writer.write("### Required\n");
        writer.write("system.peers=127.0.0.1:20200,127.0.0.1:20201\n");
        writer.write("### Required\n");
        writer.write("system.groupId=1\n");
        writer.write("### Optional. Default will search conf,config,src/main/conf/src/main/config\n");
        writer.write("system.certPath=conf,config,src/main/resources/conf,src/main/resources/config\n");
        writer.write("### Optional. If don't specify a random private key will be used\n");
        writer.write("system.hexPrivateKey=\n");
    }


    private void writeContractKeys(BufferedWriter writer) throws IOException {
        for(String contractName :contracts){
            contractName = CommonUtil.makeFirstCharLowerCase(contractName);
            writer.write("### Optional. Please fill this address if you want to use related service\n");
            writer.write("system."+contractName+"Address=\n");
        }
    }


    @Override
    public File getParentDir() {
        return this.parentDir;
    }

    @Override
    public String getName() {
        return "application.properties";
    }
}
