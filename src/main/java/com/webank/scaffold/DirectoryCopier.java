package com.webank.scaffold;

import com.webank.scaffold.util.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * 模拟contracts目录
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class DirectoryCopier {

    public void copy(String fromDir, String toDir) throws Exception{

        File srcDir = ensureSrcDir(fromDir);

        File destDir = ensureDestDir(toDir);

        IOUtil.copyFolder(srcDir, destDir);
    }

    private File ensureSrcDir(String fromDir) throws IOException{
        File srcDir = new File(fromDir);
        if(!srcDir.exists() || !srcDir.isDirectory()){
            throw new IOException(srcDir.getAbsolutePath()+" is not valid contract folder");
        }
        return srcDir;
    }

    private File  ensureDestDir(String toDir) throws IOException{
        File destDir = new File(toDir);
        destDir.mkdirs();
        if(!destDir.exists() || !destDir.isDirectory()){
            throw new IOException(destDir.getAbsolutePath()+" is not valid target folder");
        }
        if(destDir.listFiles().length != 0){
            throw new IOException(destDir.getAbsolutePath()+" not clean!");
        }
        return destDir;
    }
}
