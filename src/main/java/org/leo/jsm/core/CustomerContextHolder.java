package org.leo.jsm.core;

/**
 * <b>function:</b> 多数据源
 *
 * @author hoojo
 * @version 1.0
 * @createDate 2013-9-27 上午11:36:57
 * @file CustomerContextHolder.java
 * @package com.hoo.framework.spring.support
 * @project SHMB
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 */
public abstract class CustomerContextHolder {

    public final static String SESSION_FACTORY_WRITER = "writer";
    public final static String SESSION_FACTORY_READER = "reader";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setContextType(String contextType) {
        contextHolder.set(contextType);
    }

    public static String getContextType() {
        return contextHolder.get();
    }

    public static void clearContextType() {
        contextHolder.remove();
    }
}