package com.lwtxzwt.demo.controller;

import com.lwtxzwt.demo.annotation.Limit;
import com.lwtxzwt.demo.service.DemoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoService demoService;

    @Limit(key = "getTemperature", max = 100)
    //@Limit(key = "getTemperature", max = 1)
    @RequestMapping(value = "/getTemperature", method = RequestMethod.GET)
    public Object getTemperature(@RequestParam String province,
                                            @RequestParam String city,
                                            @RequestParam String county) {
        Map map = new HashMap();
        map.put("code", 200);
        map.put("data", demoService.getTemperature(province, city, county));
        return map;
    }
}
