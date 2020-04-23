package org.zhenchao.kratos.inject;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zhenchao.kratos.ConfigInjector;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.inject.zk.DefaultOptions;
import org.zhenchao.kratos.inject.zk.DefaultOptions2;
import org.zhenchao.kratos.util.Bytes;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author zhenchao.wang 2018-09-19 10:39
 * @version 1.0.0
 */
public class ZkConfigInjectorTest {

    private static final String ROOT_PATH = "/kratos";

    private final ConfigInjector injector = ConfigInjector.getInstance();

    private static CuratorFramework zkClient;

    @BeforeClass
    public static void init() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        zkClient.start();
    }

    @After
    public void tearDown() throws Exception {
        injector.reset();
    }

    @Test
    public void testDefaultOptionsNotLoad() throws Exception {
        String url = "https://www.zhenchao.org";
        String nums = "1, 2, 3,4,   5";
        String data = "access_url=" + url + "\nnums=" + nums;

        final String path = ROOT_PATH + "/example";
        zkClient.setData().forPath(path, Bytes.toBytes(data));

        Thread.sleep(1000);

        DefaultOptions defaultOptions = new DefaultOptions();
        injector.configureBean(defaultOptions);
        Assert.assertEquals(url, defaultOptions.getAccessUrl());
        String[] elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }

        String url1 = "https://www.zhenchao.com";
        String nums1 = "1, 2,  3";
        data = "access_url=" + url1 + "\nnums=" + nums1;
        zkClient.setData().forPath(path, Bytes.toBytes(data));
        Thread.sleep(1000);
        Assert.assertEquals(url, defaultOptions.getAccessUrl());
        elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }
    }

    @Test
    public void testDefaultOptionsAutoload() throws Exception {
        String url = "https://www.zhenchao.org";
        String nums = "1, 2, 3,4,   5";
        String data = "access_url=" + url + "\nnums=" + nums;

        final String path = ROOT_PATH + "/example";
        zkClient.setData().forPath(path, Bytes.toBytes(data));

        Thread.sleep(1000);

        DefaultOptions defaultOptions = new DefaultOptions();
        injector.configureBean(defaultOptions);
        Assert.assertEquals(url, defaultOptions.getAccessUrl());
        String[] elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }

        url = "https://www.zhenchao.com";
        nums = "1, 2,  3";
        data = Constants.COMMONS_CONFIG_AUTOLOAD + "=true\naccess_url=" + url + "\nnums=" + nums;
        zkClient.setData().forPath(path, Bytes.toBytes(data));
        Thread.sleep(1000);
        Assert.assertEquals(url, defaultOptions.getAccessUrl());
        elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }
    }

    @Test
    public void testDefaultOptions2() throws Exception {
        String url = "https://www.zhenchao.org";
        String nums = "1, 2, 3, 4, 5, 6";
        String ruleNames = "aaa, bbb,   ccc,ddd";
        String data = "access_url=" + url + "\nnums=" + nums + "\nrule.names=" + ruleNames;

        final String path = ROOT_PATH + "/example";
        zkClient.setData().forPath(path, Bytes.toBytes(data));

        Thread.sleep(1000);

        DefaultOptions defaultOptions = new DefaultOptions();
        injector.configureBean(defaultOptions);
        DefaultOptions2 defaultOptions2 = injector.configureBean(DefaultOptions2.class).getOptions(DefaultOptions2.class);

        Assert.assertEquals(url, defaultOptions.getAccessUrl());

        String[] elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }

        elems = ruleNames.split(",\\s*");
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(elems[i], defaultOptions2.getRuleNameList().get(i));
        }

        url = "https://www.zhenchao.com";
        nums = "1, 2, 3";
        ruleNames = "111, aaa, ccc.c, ddd_a";
        data = Constants.COMMONS_CONFIG_AUTOLOAD + "=true\naccess_url=" + url + "\nnums=" + nums + "\nrule.names=" + ruleNames;

        zkClient.setData().forPath(path, Bytes.toBytes(data));

        Thread.sleep(1000);

        Assert.assertEquals(url, defaultOptions.getAccessUrl());

        elems = nums.split(",\\s*");
        Assert.assertEquals(
                defaultOptions.getSet(),
                Arrays.stream(nums.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toSet()));
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(Integer.valueOf(elems[i]), defaultOptions.nums.get(i));
        }

        elems = ruleNames.split(",\\s*");
        for (int i = 0; i < elems.length; i++) {
            Assert.assertEquals(elems[i], defaultOptions2.getRuleNameList().get(i));
        }

    }

}
