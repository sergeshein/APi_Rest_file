package com.example.APi_Rest_file.util;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;

public class MainUtil {
    public static Map<String, Object> fillInTheData(String prefix, List<List<Long>> data) {
        Map<String, Object> result = new HashMap<>();

        StringBuilder msg = new StringBuilder();
        msg.append(prefix).append("_");

        for(int i = 0; i < data.size(); i++) {
            msg.append(i+1);
            result.put(msg.toString(), data.get(i));
            msg.deleteCharAt(msg.length() - 1);
        }

        return result;
    }
    public static long getHashSumFile(String filePath) {
        Adler32 adler32 = new Adler32();
        InputStream is = null;
        byte [] buf = new byte[1024];
        int len;
        try {
            is = new BufferedInputStream(new FileInputStream(filePath));

            while((len = is.read(buf)) > 0)
                adler32.update(buf, 0, len);

        } catch (IOException e) {
            throw new RequestException(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return adler32.getValue();
    }

}
