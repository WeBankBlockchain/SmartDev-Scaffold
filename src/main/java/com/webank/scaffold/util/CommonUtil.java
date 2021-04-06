package com.webank.scaffold.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/26
 */
public class CommonUtil {
    private CommonUtil(){}


    public static String makeFirstCharUpperCase(String s){
        return Character.toUpperCase( s.charAt(0)) + s.substring(1);
    }

    public static String makeFirstCharLowerCase(String s){
        return Character.toLowerCase( s.charAt(0)) + s.substring(1);
    }

    public static List<String> contracts(File abiDir, String need){
        Set<String> needContracts = Arrays.stream(need.split(",|;")).collect(Collectors.toSet());
        List<String> contractList = Arrays.stream(abiDir.listFiles())
                .map(f->f.getName().split("\\.")[0])
                .filter(c-> (need==null||need.isEmpty()) || needContracts.contains(c))
                .collect(Collectors.toList());
        return contractList;
    }


}
