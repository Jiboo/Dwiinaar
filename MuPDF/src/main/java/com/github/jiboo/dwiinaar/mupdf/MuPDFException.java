package com.github.jiboo.dwiinaar.mupdf;

public class MuPDFException extends RuntimeException {

    public MuPDFException() {
        super();
    }

    public MuPDFException(String message) {
        super(message);
    }

    public MuPDFException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
