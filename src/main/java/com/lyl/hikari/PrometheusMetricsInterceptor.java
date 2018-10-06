package com.lyl.hikari;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/5 下午5:09
 */
@Component
public class PrometheusMetricsInterceptor extends HandlerInterceptorAdapter {

    private static String labelName1 = "path";
    private static String labelName2 = "method";
    private static String labelName3 = "code";

    private static String requestCounterName;
    private static String inprogressRequestsName;
    private static String requestTimeName;
    private static String requestLatencyHistogramName;
    private static String requestLatencyName;


    private static Counter requestCounter;
    private static Gauge inprogressRequests;
    private static Gauge requestTime;
    private static Histogram requestLatencyHistogram;
    private static Summary requestLatency;
    private Histogram.Timer histogramRequestTimer;
    private Summary.Timer summaryTimer;
    private Gauge.Timer gaugeTimer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        inprogressRequests.labels(requestURI, method, String.valueOf(status)).inc();
        histogramRequestTimer = requestLatencyHistogram.labels(requestURI, method, String.valueOf(status)).startTimer();
        summaryTimer = requestLatency.labels(requestURI, method, String.valueOf(status)).startTimer();
        gaugeTimer = requestTime.labels(requestURI, method, String.valueOf(status)).startTimer();
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();

        requestCounter.labels(requestURI, method, String.valueOf(status)).inc();
        inprogressRequests.labels(requestURI, method, String.valueOf(status)).dec();
        histogramRequestTimer.observeDuration();
        summaryTimer.observeDuration();
        gaugeTimer.setDuration();
        super.afterCompletion(request, response, handler, ex);
    }

    @Value("${spring.prometheus.metric.prefix}")
    public void setMericPrefix(String mericPrefix) {
        requestCounterName = mericPrefix + "_http_requests_total";
        inprogressRequestsName = mericPrefix + "_http_inprogress_requests";
        requestTimeName = mericPrefix + "_http_requests_costTime";
        requestLatencyHistogramName = mericPrefix + "_http_requests_latency_seconds_histogram";
        requestLatencyName = mericPrefix + "_http_requests_latency_seconds_summary";

        requestCounter = Counter.build()
                .name(requestCounterName).labelNames(labelName1, labelName2, labelName3)
                .help("Total requests.").register();
        inprogressRequests = Gauge.build()
                .name(inprogressRequestsName).labelNames(labelName1, labelName2, labelName3)
                .help("Inprogress requests.").register();
        requestTime = Gauge.build()
                .name(requestTimeName).labelNames(labelName1, labelName2, labelName3)
                .help("requests cost time.").register();
        requestLatencyHistogram = Histogram.build().labelNames(labelName1, labelName2, labelName3)
                .name(requestLatencyHistogramName).help("Request latency in seconds.")
                .register();
        requestLatency = Summary.build()
                .name(requestLatencyName)
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .labelNames(labelName1, labelName2, labelName3)
                .help("Request latency in seconds.").register();

    }
}
