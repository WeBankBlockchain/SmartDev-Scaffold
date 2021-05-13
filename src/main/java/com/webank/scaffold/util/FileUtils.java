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

package com.webank.scaffold.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;

/**
 * @author marsli
 */
public class FileUtils {

    /**
     * @param content file content
     * @param fileDir file's directory
     * @param fileName fileName include suffix
     */
    public static void writeStringToFile(String content, File fileDir, String fileName) throws IOException {

        if (StringUtils.isBlank(content)) {
            return;
        }
        if (!fileDir.exists()) {
            boolean res = fileDir.mkdirs();
        }
        Path filePath = Paths.get(fileDir + File.separator + fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }


}
