package com.webank.scaffold.util;

import com.webank.scaffold.exception.ScaffoldException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import org.apache.commons.lang3.StringUtils;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
@Slf4j
public class IOUtil {
    private IOUtil(){}

    private static final int BUF_SIZE = 2048;

    public static String readAsString(File file) throws IOException {
        try(InputStream in = new FileInputStream(file)){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copy(in, baos);
            return new String(baos.toByteArray());
        }
    }

    public static String readAsString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(inputStream, baos);
        return new String(baos.toByteArray());
    }

    public static void writeString(File target, String template) throws IOException{
        ByteArrayInputStream baos = new ByteArrayInputStream(template.getBytes());
        try(FileOutputStream fos = new FileOutputStream(target, false)){
            copy(baos, fos);
        }
    }

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

    public static void copyFolder(File srcDir, final File destDir) throws IOException{
        for(File f: srcDir.listFiles()){
            File fileCopyTo = new File(destDir, f.getName());
            if(!f.isDirectory()){
                copyFile(f, fileCopyTo);
            }
            else{
                if(!fileCopyTo.mkdirs()){
                    throw new IOException("Dir "+fileCopyTo.getAbsolutePath() + " create failed");
                }
                copyFolder(f, fileCopyTo);
            }
        }

    }

    public static void copyFile(File src, File tgt) throws IOException{
        try(FileInputStream fis = new FileInputStream(src);
            FileOutputStream fos = new FileOutputStream(tgt, false)){
            copy(fis, fos);
        }
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
}
