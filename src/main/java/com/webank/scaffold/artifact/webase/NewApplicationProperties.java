/**
 * Copyright 2014-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.scaffold.artifact.webase;

import com.webank.scaffold.artifact.Artifact;
import com.webank.scaffold.artifact.webase.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.exception.ScaffoldException;
import com.webank.scaffold.util.CommonUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author marsli
 */
public class NewApplicationProperties implements Artifact {

    private File parentDir;
    private List<ContractInfo> contractInfoList;
    private String systemPeers;
    private Integer groupId;
    private String hexPrivateKey;

    /**
     * default props file without config's value
     * @param parentDir
     * @param contracts
     */
    public NewApplicationProperties(File parentDir, List<ContractInfo> contracts){
        this.parentDir = parentDir;
        this.contractInfoList = contracts;
    }

    /**
     * generate with param from webase
     * @param parentDir
     * @param contracts ContractInfo list
     * @param systemPeers 127.0.0.1:20200,127.0.0.1:20201
     * @param groupId default 1
     * @param hexPrivateKey can be empty
     */
    public NewApplicationProperties(File parentDir,
        List<ContractInfo> contracts, String systemPeers, Integer groupId, String hexPrivateKey) {
        this.parentDir = parentDir;
        this.contractInfoList = contracts;
        this.systemPeers = systemPeers;
        this.groupId = groupId;
        this.hexPrivateKey = hexPrivateKey;

    }

    @Override
    public void generate() throws Exception {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.toFile()))){
            writeSdkConnectKeys(writer);
            writeContractKeys(writer);
            writeSpringBootKeys(writer);
            writer.flush();
        }
    }
    private void writeSdkConnectKeys(BufferedWriter writer) throws IOException {
        writer.write("### Required\n");
        if (systemPeers != null) {
            writer.write("system.peers=" + this.systemPeers + "\n");
        } else {
            writer.write("system.peers=127.0.0.1:20200,127.0.0.1:20201\n");
        }
        writer.write("### Required\n");
        if (groupId != null) {
            writer.write("system.groupId=" + this.groupId + "\n");
        } else {
            writer.write("system.groupId=1\n");
        }
        writer.write("### Optional. Default will search conf,config,src/main/conf/src/main/config\n");
        writer.write("system.certPath=conf,config,src/main/resources/conf,src/main/resources/config\n");
        writer.write("### Optional. If don't specify a random private key will be used\n");
        if (hexPrivateKey != null) {
            writer.write("system.hexPrivateKey=" + this.hexPrivateKey + "\n");
        } else {
            writer.write("system.hexPrivateKey=\n");
        }
    }

    private void writeSpringBootKeys(BufferedWriter writer) throws IOException {
        writer.write("### ### Springboot server config\n");
        writer.write("server.port=8080\n");
        writer.write("server.session.timeout=60\n");
        writer.write("banner.charset=UTF-8\n");
        writer.write("spring.jackson.date-format=yyyy-MM-dd HH:mm:ss\n");
        writer.write("spring.jackson.time-zone=GMT+8\n");
    }

    private void writeContractKeys(BufferedWriter writer) throws IOException {
        if (contractInfoList == null || contractInfoList.isEmpty()) {
            throw new ScaffoldException("contractInfoList is empty!");
        }
        for(ContractInfo info :contractInfoList){
            String contractName = CommonUtil.makeFirstCharLowerCase(info.getContractName());
            writer.write("### Optional. Please fill this address if you want to use related service\n");
            if (info.getContractAddress() != null) {
                writer.write("system.contract." + contractName + "Address="
                    + info.getContractAddress() + "\n");
            } else {
                writer.write("system.contract." + contractName + "Address=\n");
            }
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


