package org.zhenchao.kratos;

import org.junit.Assert;
import org.junit.Test;
import org.zhenchao.kratos.manager.Options1;
import org.zhenchao.kratos.manager.Options2;
import org.zhenchao.kratos.manager.Options3;
import org.zhenchao.kratos.manager.Options4;
import org.zhenchao.kratos.manager.Options5;

/**
 * @author zhenchao.wang 2020-01-19 10:47
 * @version 1.0.0
 */
public class ConfigManagerTest {

    @Test
    public void initialize() throws Exception {
        final ConfigManager configManager = ConfigManager.getInstance();
        final int count = configManager.initialize("org.zhenchao.kratos.manager");
        Assert.assertEquals(4, count);
        Assert.assertNotNull(configManager.getOptions(Options1.class));
        Assert.assertNotNull(configManager.getOptions(Options2.class));
        Assert.assertNotNull(configManager.getOptions(Options3.class));
        Assert.assertNotNull(configManager.getOptions(Options4.class));
        Assert.assertNull(configManager.getOptions(Options5.class));
        Assert.assertSame(configManager.getOptions(Options1.class), configManager.getOptions(Options1.class));
        Assert.assertNotSame(configManager.getOptions(Options1.class), configManager.getOptions(Options2.class));

        // configure by manual
        final Options5 options5 = new Options5();
        configManager.getInjector().configureBean(options5);
        Assert.assertNotNull(configManager.getOptions(Options5.class));
        Assert.assertSame(options5, configManager.getOptions(Options5.class));
    }

}