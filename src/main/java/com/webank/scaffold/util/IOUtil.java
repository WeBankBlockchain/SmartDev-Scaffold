package com.webank.scaffold.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
@Slf4j
public class IOUtil {

    private IOUtil(){}

    private static final int BUF_SIZE = 2048;

    public static String readAsString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(inputStream, baos);
        return new String(baos.toByteArray());
    }

    public static void copy(InputStream is, OutputStream os) throws IOException{
        try(BufferedInputStream bis = new BufferedInputStream(is); BufferedOutputStream bos = new BufferedOutputStream(os)){
            byte[] buf = new byte[BUF_SIZE];
            int n;
            while ((n = bis.read(buf)) != -1){
                bos.write(buf, 0, n);
            }
            bos.flush();
        }
    }

    public static void removeItem(File item) {
        if(!item.isDirectory()){
            try{
                Files.delete(item.toPath());
            }catch (IOException ex){
                log.error("Failed to delete file {}",item.getAbsolutePath(),ex);
            }
            return;
        }

        for(File subItem: item.listFiles()){
            removeItem(subItem);
        }
    }

    public static void replaceAllStr(String templateName, Map<String, String> map, File dest) throws IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(templateName)){

            // 1.Read template
            String template = IOUtil.readAsString(is);

            // 2.Replace vars in template with users configuration info
            for(Map.Entry<String, String> entry : map.entrySet()){
                template = template.replace("${"+ entry.getKey() +"}", entry.getValue());
            }

            // 3.Output
            FileUtils.writeStringToFile(dest, template);
        }
    }

    public static File convertPackageToFile(final File root, String pkg){
        String[] components = pkg.split("\\.");
        File dir = root;
        for(String component: components){
            dir = new File(dir, component);
        }
        return dir;
    }
}
