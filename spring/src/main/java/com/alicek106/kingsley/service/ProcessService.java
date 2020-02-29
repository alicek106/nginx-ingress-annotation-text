package com.alicek106.kingsley.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;

@Service
public class ProcessService {
    private static final String CONF_SLEEP_ENABLED = System.getenv("CONF_SLEEP_ENABLED");

    // Span을 AOP로 대체
    // ref : https://stackoverflow.com/questions/26001006/how-can-i-log-all-of-the-request-and-header-information-for-a-spring-restcontro
    public StringBuilder printRequestInfo(HttpServletRequest req) {
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

        if (CONF_SLEEP_ENABLED.equals("true")) {
            try {
                Thread.sleep(new Random().nextInt(10) * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sb;
    }
}