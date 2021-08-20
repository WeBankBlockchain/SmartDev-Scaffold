package com.webank.scaffold.artifact.file;

import com.webank.scaffold.artifact.Artifact;
import com.webank.scaffold.constants.FileNameConstants;
import org.fisco.bcos.sdk.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
            writeJavaSdkKeys(writer);
            writeSystemConfigKeys(writer);
            writeContractKeys(writer);
            writeSpringBootKeys(writer);
            writer.flush();
        }
    }
    private void writeJavaSdkKeys(BufferedWriter writer) throws IOException {
        writer.write("### Java sdk configuration\n");
        writer.write("cryptoMaterial.certPath=conf\n");
        writer.write("network.peers[0]=127.0.0.1:20200\n");
        writer.write("network.peers[1]=127.0.0.1:20201\n");
        writer.newLine();
    }

    private void writeSystemConfigKeys(BufferedWriter writer) throws IOException {
        writer.write("### System configuration\n");
        writer.write("system.groupId=1\n");
        writer.write("system.hexPrivateKey=\n");
        writer.newLine();
    }

    private void writeContractKeys(BufferedWriter writer) throws IOException {
        writer.write("### Contract configuration\n");
        for(String contractName : contracts){
            contractName = StringUtils.lowercaseFirstLetter(contractName);
            writer.write("contract."+contractName+"Address=\n");
        }
        writer.newLine();
    }

    private void writeSpringBootKeys(BufferedWriter writer) throws IOException {
        writer.write("### Springboot configuration\n");
        writer.write("server.port=8080\n");
        writer.write("server.session.timeout=60\n");
        writer.write("banner.charset=UTF-8\n");
        writer.write("spring.jackson.date-format=yyyy-MM-dd HH:mm:ss\n");
        writer.write("spring.jackson.time-zone=GMT+8\n");
        writer.newLine();
    }

    @Override
    public File getParentDir() {
        return this.parentDir;
    }

    @Override
    public String getName() {
        return FileNameConstants.APP_PRO;
    }
}
