package org.leo.jsm.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    private int code;
    private String msg = null;

    public Message() {
    }

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
        //this.eTag = new EntityTag(String.valueOf(new Integer(this.code).hashCode()));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
