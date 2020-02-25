package com.alicek106.kingsley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
//  No need to create bean, because of autowired.
//    @Bean
//    public Tracer tracer() {
//        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
//                .withType(ConstSampler.TYPE)
//                .withParam(1);
//
//
//        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
//                .withLogSpans(true);
//
//        Configuration config = new Configuration("springboot-jaeger-tracing")
//                .withSampler(samplerConfig)
//                .withReporter(reporterConfig);
//
//        return config.getTracer();
//    }
}
