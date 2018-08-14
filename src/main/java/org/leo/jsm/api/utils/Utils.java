package org.leo.jsm.api.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

/**
 * 工具类
 */
public class Utils {

    /**
     * replace the file's path.
     * 
     * @return boolean
     */
    public static String replacePath(String path) {
        String name = path.replace("\\", File.pathSeparator);
        return name;
    }

    /**
     * 将JS转的【encodeURIComponent()】转成UTF-8.
     * 
     * @return 转换结果
     */
    public static String toUtf8(String word) {
        try {
            if (word != null && word != "") {
                return URLDecoder.decode(word, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            return word;
        }
        return word;
    }

    /**
     * 安全的toInt()方法。
     * 
     * <pre>
     * 将Object型转换成int型。
     * Null的时候，转成0。
     * </pre>
     * 
     * @param value int型数字
     * @return 三位一分割的字符串数字
     */
    public static int toInt(String value) {
        int result = 0;
        try {
            result = Integer.valueOf(value);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    /**
     * Get the file's name.
     * 
     * @return boolean
     */
    public static String getFileName(String path) {
        path = replacePath(path);
        if (path == null) {
            return "";
        }
        Integer resumeFileIndex = path.lastIndexOf(File.pathSeparator) + 1;
        String name = path.substring(resumeFileIndex, path.length());
        return name;
    }

    /**
     * 获取IP地址。
     * 
     * <pre>
     * X-Forwarded-For
     *   X-Forwarded-For:简称XFF头，它代表客户端，也就是HTTP的请求端真实的IP，
     * 只有在通过了HTTP 代理或者负载均衡服务器时才会添加该项。它不是RFC中定义的标准
     * 请求头信息，在squid缓存代理服务器开发文档中可以找到该项的详细介绍。标准格式如
     * 下：X-Forwarded-For: client1, proxy1, proxy2。
     * </pre>
     * 
     * @param request
     * @return IP地址
     */
    public static String[] getIpAddr(HttpServletRequest request) {
        String[] ip = new String[2];
        ip[0] = request.getHeader("x-forwarded-for");
        ip[1] = "x-forwarded-for";
        if (ip == null || toStr(ip[0]).length() == 0 || "unknown".equalsIgnoreCase(ip[0])) {
            ip[0] = request.getHeader("Proxy-Client-IP");
            ip[1] = "Proxy-Client-IP";
        }
        if (ip == null || toStr(ip[0]).length() == 0 || "unknown".equalsIgnoreCase(ip[0])) {
            ip[0] = request.getHeader("WL-Proxy-Client-IP");
            ip[1] = "WL-Proxy-Client-IP";
        }
        if (ip == null || toStr(ip[0]).length() == 0 || "unknown".equalsIgnoreCase(ip[0])) {
            ip[0] = request.getRemoteAddr();
            ip[1] = "local-ip";
        }
        return ip;
    }

    /**
     * 日期变换方法 (String/Timestamp).
     * 
     * <pre>
     * String(yyyy/MM/dd HH:mm:ss)型变换成Timestamp型。
     * </pre>
     * 
     * @param strDate String型日期
     * @return Timestamp型日期
     */
    public static Timestamp stringToTimestamp(String strDate) {
        return stringToTimestamp(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期变换方法 (Timestamp/String).
     * 
     * <pre>
     * Timestamp型变换成String(yyyy/MM/dd HH:mm:ss)型。
     * </pre>
     * 
     * @param timestamp Timestamp型日期
     * @return String型日期
     */
    public static String timestampToString(Timestamp timestamp) {
        return timestampToString(timestamp, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期变换方法 (String/Timestamp).
     * 
     * <pre>
     * String(yyyy/MM/dd HH:mm:ss.SSS)型变换成Timestamp型。
     * </pre>
     * 
     * @param strDate String型日期
     * @return Timestamp型日期
     */
    public static Timestamp stringToTimestampWithMs(String strDate) {
        return stringToTimestamp(strDate, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 日期变换方法 (Timestamp/String).
     * 
     * <pre>
     * Timestamp型变换成String(yyyy/MM/dd HH:mm:ss.SSS)型。
     * </pre>
     * 
     * @param timestamp Timestamp型日期
     * @return String型日期
     */
    public static String timestampToStringWithMs(Timestamp timestamp) {
        return timestampToString(timestamp, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 日期变换方法 (String/java.sql.Date).
     * 
     * <pre>
     * String(yyyy/MM/dd)型变换成java.sql.Date型。
     * </pre>
     * 
     * @param strDate String型日期
     * @return java.sql.Date型日期
     */
    public static java.sql.Date stringToDate(String strDate) {

        if (strDate == null) {
            return null;
        }

        try {
            java.sql.Date date = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(strDate);
            date = new java.sql.Date(utilDate.getTime());

            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期变换方法 (java.sql.Date/String).
     * 
     * <pre>
     * java.sql.Date型变换成String(yyyy/MM/dd)型。
     * </pre>
     * 
     * @param date java.sql.Date型日期
     * @return String型日期
     */
    public static String dateToString(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strdate = sdf.format(date);

        return strdate;
    }

    /**
     * 日期变换方法 (java.sql.Date/String).
     * 
     * <pre>
     * java.sql.Date型变换成String(yyyy/MM/dd)型。
     * </pre>
     * 
     * @param date java.sql.Date型日期
     * @return String型日期
     */
    public static String dateToStringYMD(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        strdate = sdf.format(date);

        return strdate;
    }
    
    public static String dateToStringWithMill(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HH.mm.ss.SSS");
        strdate = sdf.format(date);

        return strdate;
    }
    public static String dateToStringWithHour(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HH");
        strdate = sdf.format(date);

        return strdate;
    }
    /**
     * 取得现在日期(java.sql.Date型)方法。
     * 
     * <pre>
     * 取得现在日期(java.sql.Date型)。
     * </pre>
     * 
     * @return java.sql.Date型的日期
     */
    public static java.sql.Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * 取得现在时间(String型("yyyy/MM/dd HH:mm:ss"形式))方法。
     * 
     * <pre>
     * 取得现在时间(String型("yyyy/MM/dd HH:mm:ss"形式))。
     * </pre>
     * 
     * @return String型("yyyy/MM/dd HH:mm:ss"形式)的时间
     */
    public static String getCurrentTime() {
        return timestampToString(getCurrentTimestamp());
    }

    /**
     * 取得现在时间(String型("yyyyMMddHHmmss"形式))方法。
     * 
     * <pre>
     * 取得现在时间(String型("yyyyMMddHHmmss"形式))。
     * </pre>
     * 
     * @return String型("yyyyMMddHHmmss"形式)的时间
     */
    public static String getCurrentTimeSimple() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(getCurrentTimestamp());
    }

    /**
     * 取得现在时间(Timestamp型)方法。
     * 
     * <pre>
     * 取得现在时间(Timestamp型)。
     * </pre>
     * 
     * @return Timestamp型时间
     */
    public static Timestamp getCurrentTimestamp() {
        Calendar cal = Calendar.getInstance();
        java.util.Date date = cal.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * 取得当前时间与[UTC 1970年1月1日上午0点]的秒数的方法。
     * 
     * <pre>
     * 取得当前时间与[UTC 1970年1月1日上午0点]的秒数的。
     * </pre>
     * 
     * @return 当前时间与[UTC 1970年1月1日上午0点]的秒数
     */
    public static long getCurrentTimeInMillis() {
        long timeInMillis;
        Calendar cal = Calendar.getInstance();
        timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }

    /**
     * 安全的toString()方法。
     * 
     * <pre>
     * 将Object型转换成String型。
     * Null的时候，转成空白。
     * </pre>
     * 
     * @param decimal int型数字
     * @return 三位一分割的字符串数字
     */
    public static String toStr(Object value) {
        if (null == value) {
            return "";
        }

        return String.valueOf(value);
    }

    /**
     * 变换数字格式(添加逗号)方法。
     * 
     * <pre>
     * 将数字用逗号分割（三位一分割）。
     * </pre>
     * 
     * @param decimal int型数字
     * @return 三位一分割的字符串数字
     */
    public static String delimitComma(int decimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String strDecimal = decimalFormat.format(decimal);
        return strDecimal;
    }

    /**
     * 变换数字格式(删除逗号)方法。
     * 
     * <pre>
     * 将逗号分割的字符串转换成数字。
     * </pre>
     * 
     * @param commaStr String型字符串
     * @return int型数字
     */
    public static int commaToInt(String commaDecimal) {
        String commaless = removeComma(commaDecimal);
        return Integer.parseInt(commaless);
    }

    /**
     * 变换数字格式(删除逗号)方法。
     * 
     * <pre>
     * 将逗号分割的字符串转换成数字。
     * </pre>
     * 
     * @param commaStr String型字符串
     * @return float型数字
     */
    public static float commaToFloat(String commaDecimal) {
        String str = removeComma(commaDecimal);
        return Float.parseFloat(str);
    }

    /**
     * 变换数字格式(删除逗号)方法。
     * 
     * <pre>
     * 将逗号分割的字符串转换成数字。
     * </pre>
     * 
     * @param commaStr String型字符串
     * @return String型字符串
     */
    public static String removeComma(String commaStr) {
        String str = commaStr.replaceAll(",", "");
        return str;
    }

    /**
     * 日期变换方法 (String/Timestamp).
     * 
     * <pre>
     * String型变换成Timestamp型。
     * </pre>
     * 
     * @param strDate String型日期
     * @param format String型日期的格式
     * @return Timestamp型日期
     */
    private static Timestamp stringToTimestamp(String strDate, String format) {

        if (strDate == null) {
            return null;
        }
        try {
            Timestamp timestamp = null;

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            java.util.Date utilDate = sdf.parse(strDate);
            timestamp = new Timestamp(utilDate.getTime());

            return timestamp;
        } catch (ParseException e) {
            return null;
        }

    }

    /**
     * 日期变换方法 (Timestamp/String).
     * 
     * <pre>
     * Timestamp型变换成String型。
     * </pre>
     * 
     * @param timestamp Timestamp型日期
     * @param format String型日期的格式
     * @return String型日期
     */
    private static String timestampToString(Timestamp timestamp, String format) {

        if (timestamp == null) {
            return null;
        }
        String strdate = null;

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        strdate = sdf.format(timestamp);

        return strdate;

    }
}
