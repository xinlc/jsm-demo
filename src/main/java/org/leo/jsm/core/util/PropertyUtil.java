package org.leo.jsm.core.util;

import java.util.ResourceBundle;

public class PropertyUtil {

    /**
     * @param resource
     * @param key
     * @return
     */
    public static String getProperty(String resource, String key) {
        String propertyValue = "";

        ResourceBundle res = ResourceBundle.getBundle(resource);
        if (res.containsKey(key)) {
            propertyValue = res.getString(key);
            if (null != propertyValue && !"".equals(propertyValue)) {
                return propertyValue;
            } else {
                return null;
            }
        }

        return propertyValue;
    }

}
