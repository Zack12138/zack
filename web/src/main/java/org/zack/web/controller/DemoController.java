package org.zack.web.controller;

import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zack.utils.DateUtil;
import org.zack.utils.WeCharUtil;
import org.zack.utils.WeCharUtil2;

import java.io.File;
import java.net.URL;

@RequestMapping("demo")
@Controller
public class DemoController {

    @Autowired
    WeCharUtil2 wechar;

    @RequestMapping("path")
    @ResponseBody
    public String demo(){
        Class<DemoController> demoControllerClass = DemoController.class;
        ClassLoader classLoader = demoControllerClass.getClassLoader();
        URL resource = classLoader.getResource("config/application.yml");
        String path = resource.getPath();
        return "Hello word! "+path;
    }

    @RequestMapping("wechar")
    @ResponseBody
    public String wechar(){
        String send = wechar.send("SpringBoot Test", "SpringBoot Test");
        return send;
    }

}
