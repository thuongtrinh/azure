package com.txt.mongoredis.utils;

import liquibase.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtils {

    public static String generateFileNameWithoutTime(String prefix, String suffix) {
        String fileName = prefix;

        if (StringUtils.isNotEmpty(suffix)) {
            fileName = fileName + suffix;
        }

        return fileName;
    }

    public static void delFile(String filePath) {
        try {
            if (!StringUtil.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
