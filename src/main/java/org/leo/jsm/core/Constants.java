package org.leo.jsm.core;

import java.util.Map;
import java.util.Set;

public class Constants {
    public Map<String, String> configMap = null;
    public Map<String, String> messageMap = null;
    public Set<String> openUrlPath = null;

    public Set<String> getOpenUrlPath() {
        return openUrlPath;
    }

    public void setOpenUrlPath(Set<String> openUrlPath) {
        this.openUrlPath = openUrlPath;
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public Map<String, String> getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(Map<String, String> messageMap) {
        this.messageMap = messageMap;
    }

}
