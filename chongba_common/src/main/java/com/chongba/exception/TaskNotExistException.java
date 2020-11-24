package com.chongba.exception;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 19:16
 */
public class TaskNotExistException extends RuntimeException {

    private static final long serialVersionUID = 7674323728003042501L;

    public TaskNotExistException(final String errorMessage, final Object... args){
        super(String.format(errorMessage,args));
    }

    public TaskNotExistException(final Throwable cause){
        super(cause);
    }
}
