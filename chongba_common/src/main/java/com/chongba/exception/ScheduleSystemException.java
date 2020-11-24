package com.chongba.exception;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 19:16
 */
public class ScheduleSystemException extends RuntimeException {

    private static final long serialVersionUID = -5968578091922584757L;

    public ScheduleSystemException(final String errorMessage,final Object... args){
        super(String.format(errorMessage,args));
    }

    public ScheduleSystemException(final Throwable cause){
        super(cause);
    }
}
