/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.exception.ScaffoldException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;

/**
 * ContractsDir
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-07 11:12
 **/
@Slf4j
public class ContractsDir extends DirectoryArtifact {

    private UserConfig config;

    public ContractsDir(File parentDir, UserConfig config) {
        super(parentDir);
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        File file = FileUtils.getFile(config.getSolidityDir());
        if(checkContractsDir(file)){
            FileUtils.copyDirectory(file, this.toFile());
        }
    }

    @Override
    public String getName() {
        return DirNameConstants.SOL_Dir;
    }

    private boolean checkContractsDir(File file) throws Exception {
        if(!file.isDirectory() || file.listFiles().length == 0){
            throw new ScaffoldException("contracts directory must not be empty : " + file.getName());
        }
        for (File solFile : file.listFiles()) {
            if(!solFile.getName().endsWith(".sol")){
                throw new ScaffoldException(solFile.getName() + "  is not contract file");
            }
        }
        return true;
    }
}
