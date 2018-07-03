package edu.salleurl.lscatalunya.repositories.json;

public class JsonException extends Exception {

    //Error codes
    public final static int FORMAT_ERROR = 0;
    public final static int READ_ERROR = 1;
    public final static int ADD_ERROR = 2;
    public final static int DELETE_ERROR = 3;

    //Code
    private int errorCode;

    public JsonException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}