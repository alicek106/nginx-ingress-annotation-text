package com.alicek106.kingsley;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@SpringBootApplication
public class KingsleyApplication {
    // Below variables will be set in environment val :D:D:D:D:D by alicek106
        public final static String JAEGER_HOST = System.getenv("CONF_JAEGER_HOST");
        public final static String JAEGER_PORT = System.getenv("CONF_JAEGER_PORT");

    public static void main(String[] args) {
        System.setProperty("spring.application.name", "alicek106-spring-test");
        System.setProperty("opentracing.jaeger.udp-sender.host", KingsleyApplication.JAEGER_HOST);
        System.setProperty("opentracing.jaeger.udp-sender.port", KingsleyApplication.JAEGER_PORT);
        System.setProperty("opentracing.jaeger.enable-b3-propagation", "true");

        SpringApplication.run(KingsleyApplication.class, args);
    }

    @Aspect
    @Component
    public class SpanAspect {
        @Autowired
        private Tracer tracer;

        @Around("execution(* com.alicek106.kingsley..*.*(..))")
        public Object logging(ProceedingJoinPoint pjp) throws Throwable {
            Span span = tracer.buildSpan(pjp.getSignature().getName()).start(); // .asChildOf(parentContext).start();
            Object result = pjp.proceed();
            span.finish();
            return result;
        }
    }
}
