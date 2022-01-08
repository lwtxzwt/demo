package com.lwtxzwt.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 简单的缓存结构
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
@Data
@AllArgsConstructor
public class TreeNode {

    private String name;

    private String value;

    private List<TreeNode> children;

}
