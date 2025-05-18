package com.fluffy.blogify.exceptions;

import io.javalin.http.HttpCode;

public class CSRFTokenVerificationException extends HttpException {
    public CSRFTokenVerificationException() {
        super(HttpCode.BAD_REQUEST, "CSRF token is invalid");
    }
}
