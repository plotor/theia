package org.zhenchao.kratos.example;

import org.zhenchao.kratos.ConfigManager;

/**
 * @author zhenchao.wang 2020-04-22 17:09
 * @version 1.0.0
 */
public class KratosMain {

    public static void main(String[] args) throws Exception {
        final ConfigManager configManager = ConfigManager.getInstance();
        // 初始化配置管理器
        configManager.initialize("org.zhenchao.kratos.example");
        // 获取 options 实例
        final ExampleOptions options = configManager.getOptions(ExampleOptions.class);
        // 获取具体的配置项
        System.out.println(options.getPropMessage());
    }

}
