package me.theredcat.lib.web;

/**
 * An exception representing http error.
 */
public class HttpException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param code Http status code.
     */
    public HttpException(int code) {
        super("Http request returned error responce - " + code);
    }

}
