package com.alicek106.kingsley.controller;

import com.alicek106.kingsley.KingsleyApplication;
import com.alicek106.kingsley.service.ProcessService;
import com.couchbase.client.java.Cluster;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.huxhorn.sulky.ulid.ULID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/*")
public class TestController {
    @Autowired
    private Tracer tracer; // AOP로 바꿔서 더이상 쓰지 않음

    @Autowired
    private ProcessService processService;

    // Spinnaker ULID (execution ID) generation 확인용 ulid
    private final ULID ulid = new ULID();

    // Boolean으로 하면 더 빠르게 연산 가능하지 않을까?
    private static final String CONF_JAEGER_ENABLED = System.getenv("CONF_JAEGER_ENABLED");
    private static Cluster cluster; // Couchbase test용
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController(){
        logger.info(String.format("CONF_JAEGER_ENABLED : %s", TestController.CONF_JAEGER_ENABLED));
        logger.info(String.format("CONF_JAEGER_HOST : %s", KingsleyApplication.JAEGER_HOST));
        logger.info(String.format("CONF_JAEGER_PORT : %s", KingsleyApplication.JAEGER_PORT));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String slash(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        // String uid = ulid.nextValue().toString().toLowerCase();
        return processService.printRequestInfo(req).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red")
    public String red(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return processService.printRequestInfo(req).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/red/apple")
    public String redApple(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
        return processService.printRequestInfo(req).toString();
    }
}