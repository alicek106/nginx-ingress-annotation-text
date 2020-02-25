package com.alicek106.kingsley.controller;

import com.alicek106.kingsley.KingsleyApplication;
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
import com.alicek106.kingsley.KingsleyApplication;

@RestController
@RequestMapping("/*")
public class testController {
    @Autowired
    private Tracer tracer;

    // Spinnaker ULID (execution ID) generation 확인용 ulid
    private final ULID ulid = new ULID();

    // Boolean으로 하면 더 빠르게 연산 가능하지 않을까?
    private static final String CONF_JAEGER_ENABLED = System.getenv("CONF_JAEGER_ENABLED");
    private static final String CONF_SLEEP_ENABLED = System.getenv("CONF_SLEEP_ENABLED");
    private static Cluster cluster; // Couchbase test용
    private static final Logger logger = LoggerFactory.getLogger(testController.class);

    public testController(){
        logger.info(String.format("CONF_JAEGER_ENABLED : %s", testController.CONF_JAEGER_ENABLED));
        logger.info(String.format("CONF_SLEEP_ENABLED : %s", testController.CONF_SLEEP_ENABLED));
        logger.info(String.format("CONF_JAEGER_HOST : %s", KingsleyApplication.JAEGER_HOST));
        logger.info(String.format("CONF_JAEGER_PORT : %s", KingsleyApplication.JAEGER_PORT));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String slash(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        // String uid = ulid.nextValue().toString().toLowerCase();
        return processRequest(req).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red")
    public String red(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return processRequest(req).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red/apple")
    public String redApple(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return processRequest(req).toString();
    }

    // AOP로 할 수 있을까?
    private StringBuilder processRequest(HttpServletRequest req){
        StringBuilder sb;
        if(testController.CONF_JAEGER_ENABLED.equals("true")){
            // span start!
            Span span = tracer.buildSpan("alicek106-span-spring").start(); // .asChildOf(parentContext).start();

            sb = printRequestInfo(req);

            // span ended
            span.finish();
        }else{
            sb = printRequestInfo(req);
        }
        return sb;
    }

    // ref : https://stackoverflow.com/questions/26001006/how-can-i-log-all-of-the-request-and-header-information-for-a-spring-restcontro
    private StringBuilder printRequestInfo(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder("------------------------\n");
        StringBuffer requestURL = req.getRequestURL();
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

        if(testController.CONF_SLEEP_ENABLED.equals("true")){
            try {
                Thread.sleep(new Random().nextInt(10) * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sb;
    }
}