package com.lwtxzwt.demo.service.impl;

import com.lwtxzwt.demo.constants.TreeNode;
import com.lwtxzwt.demo.constants.URLConstants;
import com.lwtxzwt.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    RestTemplate restTemplate;

    private final Map<String, TreeNode> cacheMap = new ConcurrentHashMap<>();

    @Retryable(value = RestClientException.class, maxAttempts = 3,
            backoff = @Backoff(delay = 1000L,multiplier = 2))
    @Override
    public Optional<Integer> getTemperature(String province, String city, String county) {
        String code =getCode(province, city, county);
        Map<String, Map<String, String>> res = restTemplate.getForObject(URLConstants.GET_TEMPERATURE + code + URLConstants.HTML, Map.class);
        return Optional.of((int) Math.ceil(Double.valueOf(res.get("weatherinfo").get("temp"))));
    }

    private String getCode(String province, String city, String county) {
        if (cacheMap.isEmpty()) {
            Map<String, String> provinceRes = restTemplate.getForObject(URLConstants.GET_PROVINCE, Map.class);
            provinceRes.keySet().forEach(e -> {
                cacheMap.put(provinceRes.get(e), new TreeNode(provinceRes.get(e), e, new ArrayList<>()));
            });
        }

        if (cacheMap.get(province) == null) {
            throw new RuntimeException("province error");
        }

        TreeNode provinceNode = cacheMap.get(province);
        String provinceCode = provinceNode.getValue();
        if (provinceNode.getChildren().isEmpty()) {
            Map<String, String> cityRes = restTemplate.getForObject(URLConstants.GET_CITY + provinceCode + URLConstants.HTML, Map.class);
            provinceNode.setChildren(cityRes.keySet().stream().map(e ->
                    new TreeNode(cityRes.get(e), e, new ArrayList<>())).collect(Collectors.toList()));
        }

        TreeNode cityNode = provinceNode.getChildren().stream().filter(e -> e.getName().equals(city)).findFirst().orElse(null);
        if (cityNode == null) {
            throw new RuntimeException("city error");
        }

        String cityCode = cityNode.getValue();
        if (cityNode.getChildren().isEmpty()) {
            Map<String, String> countyRes = restTemplate.getForObject(URLConstants.GET_COUNTY + provinceCode + cityCode + URLConstants.HTML, Map.class);
            cityNode.setChildren(countyRes.keySet().stream().map(e ->
                    new TreeNode(countyRes.get(e), e, new ArrayList<>())).collect(Collectors.toList()));
        }

        TreeNode countyNode = cityNode.getChildren().stream().filter(e -> e.getName().equals(county)).findFirst().orElse(null);
        if (countyNode == null) {
            throw new RuntimeException("county error");
        }

        String countyCode = countyNode.getValue();
        return provinceCode + cityCode + countyCode;
    }
}
