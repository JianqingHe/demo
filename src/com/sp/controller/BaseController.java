package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hejq
 * @date 2018-10-10 10:46
 */
public class BaseController {

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpServletRequest request;
}
