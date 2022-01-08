package com.lwtxzwt.demo.service;

import java.util.Optional;

/**
 * 服务类
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
public interface DemoService {

    Optional<Integer> getTemperature(String province, String city, String county);

}
