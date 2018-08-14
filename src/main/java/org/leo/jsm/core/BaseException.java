package org.leo.jsm.core;

public class BaseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8584478680691423063L;

    private int code;

    public BaseException() {
        super();
        this.code = -1;//not business error
    }

    public BaseException(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
