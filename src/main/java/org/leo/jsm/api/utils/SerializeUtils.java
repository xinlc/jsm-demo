package org.leo.jsm.api.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * 序列化、反序列化操作类
 */
public class SerializeUtils {

    /**
     * 日志处理
     */
    private final static Logger logger = Logger.getLogger(SerializeUtils.class);

    /**
     * 序列化
     *
     * @param object 序列化对象
     * @return 序列化结果
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param object 反序列化对象
     * @return 反序列化结果
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
