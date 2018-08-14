package org.leo.jsm.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * get rand number by date
     *
     * @return
     */
    public static String getRand() {
        String return_str = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        int randi = 0;
        while (randi < 1000) {
            randi = getRandId();
        }
        return_str = df.format(new Date()) + randi;
        return return_str;
    }

    /**
     * get rand ID from 0-9999
     *
     * @return
     */
    public static int getRandId() {
        return new Random().nextInt(9999);
    }

    /**
     * get rand number from 0-max
     *
     * @param max
     * @return
     */
    public static int getRandId(int max) {
        return new Random().nextInt(max);
    }

    /**
     * get rand name that like 'RandNamexxxx'
     *
     * @return
     */
    public static String getRandName(String randNamePrefix) {
        StringBuffer rand = new StringBuffer(randNamePrefix);
        rand.append(new Random().nextInt(9999));
        return rand.toString();
    }

    /**
     * check the String is the correct mail format
     *
     * @param mail
     * @return
     */
    public static boolean checkMail(String mail) {
        return mail.matches("^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    /**
     * Trim string
     *
     * @param String trim string
     * @return String after trim if str = null ,return ""
     */
    public static String trim(String str) {
        String return_str = null;
        if ((str == null) || (str == "null") || (str.equals(""))) {
            return_str = "";
        } else {
            return_str = str.toString().trim();
        }
        return return_str;
    }


    public static Map<String, String> propsToMap(Properties props,
                                                 String idPatternString, String valuePatternString) {

        Map<String, String> map = new HashMap<String, String>();

        Pattern idPattern = Pattern.compile(idPatternString);

        for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
            String key = (String) en.nextElement();
            String mapKey = (String) props.getProperty(key);

            if (mapKey != null) {
                Matcher idMatcher = idPattern.matcher(key);
                if (idMatcher.matches()) {

                    String valueName = valuePatternString.replace("{idGroup}",
                            idMatcher.group(1));
                    String mapValue = props.getProperty(valueName);

                    if (mapValue != null) {
                        map.put(mapKey, mapValue);
                    }
                }
            }
        }

        return map;
    }


    public static String formatDate(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDateStr(String dateStr, String format) throws ParseException {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        return date;
    }

    public static Timestamp parseTimestampStr(String dateStr, String format) throws ParseException {
        return new Timestamp(parseDateStr(dateStr, format).getTime());
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static void main(String[] args) throws Exception {
    }
}
