package com.webank.scaffold.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class IOUtil {

    private IOUtil() {
    }

    public static void replaceAllStr(String templateName, Map<String, String> map, File dest) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(templateName)) {

            // 1.Read template
            String template = IOUtils.toString(is);

            // 2.Replace vars in template with users configuration info
            for (Map.Entry<String, String> entry : map.entrySet()) {
                template = template.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            // 3.Output
            FileUtils.writeStringToFile(dest, template);
        }
    }

    public static File convertPackageToFile(final File root, String pkg) {
        String[] components = pkg.split("\\.");
        File dir = root;
        for (String component : components) {
            dir = new File(dir, component);
        }
        return dir;
    }
    
}
