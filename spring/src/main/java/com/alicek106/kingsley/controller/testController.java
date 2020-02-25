package com.alicek106.kingsley.controller;

import com.couchbase.client.java.Cluster;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.huxhorn.sulky.ulid.ULID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/*")
public class testController {
    @Autowired
    private Tracer tracer;

    private final ULID ulid = new ULID();
    private static final Logger logger = LoggerFactory.getLogger(testController.class);
    private static Cluster cluster;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String slash(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        // String uid = ulid.nextValue().toString().toLowerCase();
        return printRequestInfo(req, headers).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red")
    public String red(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return printRequestInfo(req, headers).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red/apple")
    public String redApple(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return printRequestInfo(req, headers).toString();
    }

    // ref : https://stackoverflow.com/questions/26001006/how-can-i-log-all-of-the-request-and-header-information-for-a-spring-restcontro
    private StringBuilder printRequestInfo(HttpServletRequest req, HttpHeaders headers) {
        // span start!
        Span span = tracer.buildSpan("alicek106-span-spring").start(); // .asChildOf(parentContext).start();

        StringBuilder sb = new StringBuilder("------------------------\n");
        StringBuffer requestURL = req.getRequestURL();
        String queryString = req.getQueryString();

        sb.append(String.format("You accessed to path \"%s\"\n", requestURL.toString()));

        try {
            sb.append(String.format("Container Hostname %s\n", InetAddress.getLocalHost().getHostName()));
            sb.append(String.format("Container IP : %s\n", InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sb.append(String.format("Original IP with Proxy : %s\n\n", req.getRemoteAddr()));

        sb.append("------------------------\n");
        sb.append("Spring received HTTP header\n");

        Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            sb.append(String.format("%s: %s\n", headerName, req.getHeader(headerName)));
        }

        sb.append("\n------------------------\n");
        System.out.println(sb.toString());

        try {
            Thread.sleep(new Random().nextInt(10) * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // span ended
        span.finish();
        return sb;
    }
}