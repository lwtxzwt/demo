package com.lwtxzwt.demo.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * HttpMessage转换
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
public class HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public HttpMessageConverter() {
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypeList);
    }
}
