package org.leo.jsm.api.utils;

import java.util.ResourceBundle;

/**
 * 读取资源文件
 */
public class PropertyUtils {

    /**
     * 应用程序相关配置文件
     */
    private static ResourceBundle appResource = null;

    /**
     * 应用程序相关配置文件名
     */
    private static String appFileName = "app";

    /**
     * 提示信息相关配置文件
     */
    private static ResourceBundle msgResource = null;

    /**
     * 提示信息相关配置文件名
     */
    private static String msgFileName = "ValidationMessages";

    /**
     * 画面文字显示相关配置文件
     */
    private static ResourceBundle labelResource = null;

    /**
     * 画面文字显示相关配置文件名
     */
    private static String labelFileName = "LabelMessages";

    /**
     * redis相关配置文件
     */
    private static ResourceBundle redisResource = null;

    /**
     * redis相关配置文件名
     */
    private static String redisFileName = "redis";

    /**
     * Mq配置文件
     */
    private static ResourceBundle mqResource = null;
    /**
     * Mq配置文件
     */
    private static String mqFileName = "mq";

    /**
     * Config配置文件
     */
    private static ResourceBundle configResource = null;
    /**
     * Config配置文件
     */
    private static String configFileName = "config";


    /**
     * 取得应用程序相关配置信息
     *
     * @param key KEY值
     * @return KEY对应的VALUE值
     */
    public static String getApp(String key) {
        String message = null;
        if (appResource == null) {
            appResource = ResourceBundle.getBundle(appFileName);
        }
        message = appResource.getString(key);
        return message;
    }

    /**
     * 取得应用程序相关配置信息
     *
     * @param key KEY值
     * @return KEY对应的VALUE值
     */
    public static String getConfig(String key) {
        String message = null;
        if (configResource == null) {
            configResource = ResourceBundle.getBundle(configFileName);
        }
        message = configResource.getString(key);
        return message;
    }

    /**
     * 取得提示信息相关配置信息
     *
     * @param key    KEY值
     * @param params 信息对应的参数【{0}】
     * @return KEY对应的VALUE值
     */
    public static String getMsg(String key, String... params) {
        String message = null;
        if (msgResource == null) {
            msgResource = ResourceBundle.getBundle(msgFileName);
        }
        try {
            message = msgResource.getString(key);
            for (String item : params) {
                String replaceParam = message.substring(message.lastIndexOf("{"), message.lastIndexOf("}") + 1);
                message.replaceAll(replaceParam, item);
            }
        } catch (Exception e) {
            message = key;
        }
        return message;
    }

    /**
     * 取得画面文字显示相关配置信息
     *
     * @param key KEY值
     * @return KEY对应的VALUE值
     */
    public static String getLabel(String key) {
        String message = null;
        if (labelResource == null) {
            labelResource = ResourceBundle.getBundle(labelFileName);
        }
        message = labelResource.getString(key);
        return message;
    }

    /**
     * 取得redis相关配置信息
     *
     * @param key KEY值
     * @return KEY对应的VALUE值
     */
    public static String getRedis(String key) {
        String message = null;
        if (redisResource == null) {
            redisResource = ResourceBundle.getBundle(redisFileName);
        }
        try {
            message = redisResource.getString(key);
        } catch (Exception e) {
            message = "";
        }
        return message;
    }

    /**
     * 取得应用程序相关配置信息
     *
     * @param key KEY值
     * @return KEY对应的VALUE值
     */
    public static String getMQ(String key) {
        String message = null;
        if (mqResource == null) {
            mqResource = ResourceBundle.getBundle(mqFileName);
        }
        message = mqResource.getString(key);
        return message;
    }
}
