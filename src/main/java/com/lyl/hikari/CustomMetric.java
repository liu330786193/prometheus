package com.lyl.hikari;

import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/5 下午5:04
 */
@Component
public class CustomMetric {

    static final Counter requests = Counter.build().name("my_request_total").help("Total request.")
            .labelNames("method").register();
    public void processRequest(String method){
        requests.labels(method.toUpperCase()).inc();
    }

}
