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

import com.webank.scaffold.artifact.DirectoryArtifact;
import com.webank.scaffold.util.IOUtil;
import java.io.File;
import java.util.Map;
import lombok.Getter;

/**
 * @author marsli
 */
@Getter
public class NewConfDir extends DirectoryArtifact {

    private static final String CONF_DIR = "conf";
    private Map<String, String> sdkContentMap;

    public NewConfDir(File path, Map<String, String> sdkContentMap) {
        super(path);
        this.sdkContentMap = sdkContentMap;
    }

    /**
     * write each content to each file in conf/ or conf/gm/
     * gm: gmca.crt, gmsdk.crt, gmsdk.key
     * else: ca.crt, sdk.crt, sdk.key
      */
    @Override
    protected void doGenerateSubContents() throws Exception {
        if (sdkContentMap == null || sdkContentMap.isEmpty()) {
            return;
        }
        for (String fileName : this.sdkContentMap.keySet()) {
            File certPath = new File(this.getParentDir().getPath() + File.separator + CONF_DIR);
            if (fileName.contains("gm")) {
                certPath = new File(certPath.getPath() + File.separator + "gm");
            }
            String fileContent = sdkContentMap.get(fileName);
            IOUtil.writeStringToFile(fileContent, certPath, fileName);
        }
    }

    @Override
    public String getName() {
        return CONF_DIR;
    }
}

