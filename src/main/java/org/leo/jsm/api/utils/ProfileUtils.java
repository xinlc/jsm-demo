package org.leo.jsm.api.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.leo.jsm.api.exception.RequestException;

public class ProfileUtils {

    public static final String DEFAULT_AVATAR_PATH = "/data/wwwroot/attachment/face";
    //public static final String DEFAULT_AVATAR_PATH = "c:/Jeff";
    public static final String DEFAULT_FEEDBACK_PATH = "/data/wwwroot/attachment/other";

    /**
     * 验证用户id
     *
     * @param profile
     * @throws RequestException
     */
    public static void verifyUserId(String userId) throws RequestException {
        if (null == userId || "".equals(userId)) {
            // 验证用户ID是否为空
            throw new RequestException(900002);
        } else if (!isNumeric(userId)) {
            // 验证用户ID是否为数字
            throw new RequestException(700001);
        }
    }

    public static String getCurrentTimeStampStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
        String date = df.format(new Date());
        return date;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
}
