package com.webank.scaffold.core.util;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class PackageNameUtil {


    public static String getRootPackageName(String group, String artifact){
        return group + "." +artifact;
    }

    public static String getRootPackageName(UserConfig config){
        String group = config.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifact = config.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        return getRootPackageName(group, artifact);
    }

    public static String getUtilsPackageName(UserConfig config){
        return getRootPackageName(config) +".utils";
    }

    public static String getConfigPackageName(UserConfig config){
        return getRootPackageName(config) +".config";
    }

    public static String getBOPackageName(UserConfig config){
        return getRootPackageName(config) + ".model.bo";
    }

    public static String getServicePackageName(UserConfig config){
        return getRootPackageName(config) + ".service";
    }

    /**
     * com.webank.code -> com/webank/code
     * @param root
     * @param pkg
     * @return
     */
    public static File convertPackageToFile(final File root, String pkg){
        String[] components = pkg.split("\\.");
        File dir = root;
        for(String component: components){
            dir = new File(dir, component);
        }
        return dir;
    }
}
