package com.lyl.hikari;

import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/5 下午5:08
 */
@Configuration
public class MonitoringConfig {

    @Bean
    ServletRegistrationBean servletRegistrationBean() {

        return new ServletRegistrationBean(new MetricsServlet(), "/metrics");
    }

}
