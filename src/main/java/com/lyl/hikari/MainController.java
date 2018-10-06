package com.lyl.hikari;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/5 下午5:05
 */
@RestController
public class MainController {

    @Autowired
    CustomMetric customMetric;

    @RequestMapping(value="/")
    public String home(){
        return "Hello world!";
    }

    @RequestMapping(value="/a")
    public String a(){
        return "Hello world!";
    }

    @RequestMapping(value="/b")
    public String b(){
        return "Hello world!";
    }

    @RequestMapping(value="/c")
    public String c(){
        return "Hello world!";
    }

    @RequestMapping(value="/d")
    public String d(){
        return "Hello world!";
    }

    @RequestMapping(value="/m")
    public String m(){
        List<byte[]> list = new ArrayList<>(20);
        for (int i = 0; i< 47; i++){
            byte[] bytes = new byte[1024];
            list.add(bytes);
        }
        return "Hello world!";
    }

}
