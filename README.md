# kratos

Kratos 是一个注解式配置加载与注入框架，旨在以注解的方式加载任何可以被表示成 [properties](https://docs.oracle.com/cd/E23095_01/Platform.93/ATGProgGuide/html/s0204propertiesfileformat01.html) 格式的配置，并注入给目标对象，同时支持当配置内容发生变更时回调更新。配置文件的来源可以是本地文件，也可以来自网络。Kratos 默认实现了从 ClassPath 加载本地配置文件，并支持以 SPI 的方式扩展以支持更多的配置来源，例如从 ZK 加载配置等。

特性一览：

- 支持以注解的方式加载多种配置数据源，并注入给配置对象。
- 支持预注入，预注入会校验配置的合法性，如果不合法则会放弃注入，避免配置出错影响服务。
- 支持配置变更时的回调更新，并允许用户配置是否启用。
- 内置基本类型转换器，用于将 String 类型配置项转换成目标类型。
- 支持自定义类型转换器，以实现一些定制化的类型转换。
- 支持以原生字符串或 Properties 对象的形式注入。
- 支持监听注入过程（InjectEventListener）和更新过程（UpdateEventListener）。
- 支持加载系统环境变量，并注入给配置对象。
- 支持 `${}` 占位符替换，使用指定的配置项替换占位符。
- 支持以 SPI 的方式扩展以支持更多类型的配置数据源。

---

使用文档：

- [快速接入](#快速接入)
- [使用指南](#使用指南)
- [如何扩展](#如何扩展)
- [实现原理](#实现原理)
- [注意事项](#注意事项)
- [鸣谢](#鸣谢)

### 快速接入

这里以加载并注入 ClassPath 配置文件 configurable_options.properties 为例，使用上分为 4 步：

1. 定义一个实现了 Options 接口的配置类 ExampleOptions；
2. 为 ExampleOptions 类添加 `@Configurable` 注解，用于指定配置数据源路径；
3. 调用 `ConfigManager#initialize` 方法初始化所有被管理的配置项，只允许初始化一次；
4. 调用 `ConfigManager#getOptions` 方法拿到目标 options 实例，以获取对应的配置信息。

ExampleOptions 的部分实现如下，完整实现可以参考源码：

```java
@Configurable(Constants.CP_PREFIX + "configurable_options")
public class ExampleOptions extends AbstractOptions {

    private static final long serialVersionUID = -8145624960779711094L;

    @Attribute(name = "myFiles")
    private File[] files;

    @Attribute(defaultValue = "15")
    private int number;

    @Attribute(name = "property.message")
    private String propMessage;

    @Attribute(defaultValue = "1780000")
    public long longValue;

    @Attribute(name = "another.long.value", defaultValue = "1000000")
    public long anotherLongValue;

    private Double floatingPointNumber;

    @Attribute
    private String fieldMessage;

    @Attribute
    private Boolean trueFalse;

    @Attribute(name = "list", converter = ListConverter.class)
    public List<String> list;

    @Attribute(converter = SetConverter.class)
    public Set<String> set;

    // ... 省略部分实现

    @Override
    public void update() {
        // 当配置发生变更时回调执行此方法
    }

    @Override
    public boolean validate() {
        // 此处实现配置校验逻辑
    }

}
```

初始化配置管理器：

```java
final ConfigManager configManager = ConfigManager.getInstance();
// 初始化配置管理器
configManager.initialize("org.zhenchao.kratos.example");
// 获取 options 实例
final ExampleOptions options = configManager.getOptions(ExampleOptions.class);
// 获取具体的配置项
System.out.println(options.getPropMessage());
```

好啦，就这么简单，接下去就可以愉快的使用配置项啦！

### 使用指南

本小节针对快速接入中的各个步骤进行详细说明。首先来看 __步骤 1__，对于需要需要注入的 options，需要先实现 Options 接口，或继承 AbstractOptions 抽象类，Options 接口定义如下：

```java
public interface Options extends Serializable {

    /**
     * This method will be invoked after successfully injected.
     */
    void update();

    /**
     * Validate that the configuration is correctly.
     *
     * @return {@code true} means correctly, or {@code false}.
     */
    boolean validate();

}
```

其中 `Options#update` 方法会在成功完成注入时回调，可以用于对配置字段的二次解析。方法 `Options#validate` 需要由应用自己实现对于配置的合法性校验，该方法会在预注入时调用，如果返回 false 则会放弃后续的正式注入，并抛出异常。

然后（__步骤 2__），需要使用 `@Configurable` 注解为 options 关联对应的数据源，该注解定义如下：

```java
public @interface Configurable {

    /** The configuration resource, eg. ZK:/kratos/example */
    String resource() default "";

    /** Alias for {@link #resource()} */
    String value() default "";

    /**
     * Auto configure setting.
     *
     * {@code true} means the options will be detected and auto injected,
     * otherwise you should instantiate and configure the options by manual.
     */
    boolean autoConfigure() default true;

    /**
     * Autoload configuration when found source update.
     * {@code true} means ignore the {@link Constants#COMMONS_CONFIG_AUTOLOAD} config,
     * default is {@link false}.
     */
    boolean autoload() default false;

}
```

配置项 `Configurable#autoConfigure` 默认为 true，表示允许 ConfigManager 在初始化时自动实例化并注入配置项值，否则需要由开发人员自己完成实例化，并主动调用 `ConfigInjector#configureBean(Options)` 方法完成配置项值的注入。

配置项 `Configurable#autoload` 默认为 false，当设置为 true 时则会在每次配置变更时回调执行 `Options#update` 方法，而忽略 `__commons_config_autoload` 配置。该配置项主要应用于加载 raw text 的场景，此时源配置不满足 properties 格式，所以不能简单的添加 `__commons_config_autoload=true` 配置项来控制是否回调更新，这种场景下可以通过 `Configurable#autoload` 配置项来默认启用更新。

完成与数据源的关联之后，接下来（__步骤 3__）需要使用 `@Attribute` 注解为各个字段关联对应的配置项，注解定义如下：

```java
public @interface Attribute {

    /** Property name */
    String name() default "";

    /** Alias for {@link #name()} */
    String value() default "";

    /**
     * Configure required.
     *
     * {@code true} means this field must be configured, otherwise will throw {@link ConfigException}.
     */
    boolean required() default true;

    /** The default value when missing config. */
    String defaultValue() default "";

    /** Whether inject this field with {@link java.util.Properties} or {@link String} raw type. */
    boolean raw() default false;

    /** Convert the string to target field type. */
    Class<? extends Converter> converter() default VoidConverter.class;

}
```

各个配置项说明如下：

- `name` 和 `value`：用于将当前 field 与对应的配置项名称进行关联，如果未指定则以当前属性名称作为配置项名称，强烈建议配置。
- `required`：表示当前配置项是必须的，默认为 true，如果未指定默认值，且对应的配置项缺失则会抛出 ConfigException 异常。
- `defaultValue`：默认值，如果对应的配置项缺失，则采用默认值注入。
- `raw`：是否以原生类型（String 或 Properties）进行注入，需要注意的是，一个 options 中只能定义一个 `raw=true` 的配置项，且与一般的注入方式互斥。
- `converter`：自定义类型转换器，会将 String 类型转换成目标类型后再进行注入。

注解 `@Attribute` 可以修饰 field，也可以修饰 getter 或 setter 方法，如果未明确指定 `name`，则会基于注解的属性或方法（getter 或 setter）自动计算 `name` 值，但是强烈建议手动配置 `name` 值，避免出错。类型转换器不是必须的，配置库内置了对以下类型的自动转换：

类型 | 转换器 | 说明
--- | --- | ---
boolean | BooleanConverter | 用于将字符串转换成 boolean 类型
char | CharacterConverter | 用于将字符串转换成 char 类型，提取字符串的首字母
byte | NumberConverter | 用于将字符串转换成 byte 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
short | NumberConverter | 用于将字符串转换成 short 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
int | NumberConverter | 用于将字符串转换成 int 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
long | NumberConverter | 用于将字符串转换成 long 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
float | NumberConverter | 用于将字符串转换成 float 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
double | NumberConverter | 用于将字符串转换成 double 类型，可以使用 `@NumberRadix` 指定原始值的进制类型，默认为 10 进制
String | StringConverter | 以字符串类型进行注入，区别于 raw 类型的 String 注入，后者使用整个配置文件进行注入
Array | ArrayConverter | 用于将字符串按照英文逗号进行分割，并转换成目标数组类型，仅支持一维数组转换
Date | DateConverter | 用于将字符串转换成 Date 类型，需要指定 `@DatePattern`
Calendar | CalendarConverter | 用于将字符串转换成 Calendar 类型，依赖 DateConverter
Object | GenericConverter | 将字符串转换成目标类型，相应的类需要具备一个包含 String 类型参数的构造方法

以上转换器无需手动指定，配置库会依据目标类型自动检测，如果手动指定了类型转换器，则优先级更高。

最后（__步骤 4__），需要调用 `ConfigManager#initialize` 方法初始化和注入所有的配置项，如下：

```java
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
```

ConfigManager 在执行初始化（即调用 `ConfigManager#initialize` 方法）时允许指定扫描 Options 的根包名，如果没有设置则会扫描所有的包，推荐设置。

ConfigManager 提供了 `ConfigManager#getOptions` 方法用于依据类型获取对应的 options 实例。

工具类 Parser 定义了 `Parser#toList` 和 `Parser#toSet` 方法，抽象了字符串数组到 List 和 Set 类型的转换，可以依据场景考虑使用。

最后来聊聊监听机制，配置库定义了两种监听器：InjectEventListener 和 UpdateEventListener。其中，InjectEventListener 用于监听注入过程，定义如下：

```java
public interface InjectEventListener extends EventListener {

    /**
     * This method will be invoked before injection.
     *
     * @param options The options bean that will be injected.
     */
    void prevHandle(final Options options);

    /**
     * This method will be invoked after injection.
     *
     * @param options The options bean that has been injected.
     */
    void postHandle(final Options options);

}
```

此类监听器会在执行注入过程前后被调用，可以调用 `ConfigInjector#registerInjectListener` 方法和 `ConfigInjector#removeInjectListener` 方法分别注册和注销监听器。

UpdateEventListener 则用于监听更新过程，定义如下：

```java
public interface UpdateEventListener extends EventListener {

    /**
     * This method will be invoked before update.
     *
     * @param options The options bean that will be updated.
     */
    void prevHandle(Options options);

    /**
     * This method will be invoked after update.
     *
     * @param options The options bean that has been updated.
     */
    void postHandle(Options options);

}
```

此类监听器会在调用 `Options#update` 方法前后被调用，可以调用 `ConfigInjector#registerUpdateListener` 方法和 `ConfigInjector#removeUpdateListener` 方法分别注册和注销监听器。

### 如何扩展

除了内建对 ClassPath 路径下配置的加载，Kratos 还允许用户对支持的配置数据源进行扩展。接入一个新的数据源只需要继承 AbstractSourceProvider 抽象类即可，然后在项目的 `/META-INF/services` 目录下新建一个名为 `org.zhenchao.kratos.source.provider.SourceProvider` 的文件，添加以下内容：

```text
org.zhenchao.kratos.source.provider.ClasspathSourceProvider
// your source provider class name here
```

配置库基于 jdk 内置的 SPI 机制加载所有的 SourceProvider。最后调用 `ConfUtils#registerPrefix` 静态方法注册对应的 prefix 标识即可。

下面以从 zookeeper 加载配置为例演示如何实现扩展，首先继承 AbstractSourceProvider 实现一个 ZkSourceProvider，如下：

```java
public class ZkSourceProvider extends AbstractSourceProvider implements SourceProvider {

    private final CuratorFramework zkClient;

    private final Set<Source> sourceRegistry = new HashSet<>();

    public ZkSourceProvider() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        this.zkClient.start();
    }

    @Override
    protected Properties doLoadProperties(final Source source, final PropertiesBuilder builder) throws ConfigException {
        final Class<?> optionsClass = source.getOptionsClass();
        final String resourceName = this.resourceName(source);
        log.info("Load zk configuration, resource[{}], options[{}].", resourceName, optionsClass);

        try {
            final String zkPath = this.toZkPath(resourceName);
            final byte[] bytes = zkClient.getData().forPath(zkPath);
            if (null == bytes || 0 == bytes.length) {
                log.warn("No zk property value resolved, path[{}].", zkPath);
                return builder.build();
            }
            final String data = Bytes.toString(bytes);
            if (StringUtils.isBlank(data)) {
                log.warn("No zk property value resolved, path[{}].", zkPath);
                return builder.build();
            }

            if (log.isDebugEnabled()) {
                log.debug("Get zk property, path[{}], value[{}].", zkPath, data);
            }

            final Properties properties = this.toProperties(data);
            if (!properties.isEmpty()) {
                builder.addAll(ConfUtils.toMap(properties));
            }
            return builder.build();
        } catch (Throwable t) {
            log.error("Load zk configuration error, resource[{}], optionsClass[{}]", resourceName, optionsClass, t);
            throw new ConfigException("load zk configuration error, " +
                    "resource: " + resourceName + ", options: " + optionsClass, t);
        }

    }

    @Override
    public void postLoad(Source source) {
        if (!this.tryRegisterListener(source)) {
            throw new IllegalStateException("register zk listener error, " +
                    "resource: " + source.getResourceName() + ", options: " + source.getOptionsClass());
        }
    }

    @Override
    protected String resourceName(Source source) {
        String resourceName = source.getResourceName();
        Validate.isTrue(ConfUtils.isZkResource(resourceName), "invalid zk resource name: " + resourceName);
        return resourceName;
    }

    @Override
    public boolean support(Source source) {
        return StringUtils.startsWithIgnoreCase(super.resourceName(source), Constants.ZK_PREFIX);
    }

    @Override
    public int priority() {
        return 0;
    }

    private String toZkPath(String resourceName) {
        return resourceName.substring(Constants.ZK_PREFIX.length()).trim();
    }

    /**
     * Register zk data change listener.
     *
     * @param source
     * @return
     */
    private boolean tryRegisterListener(final Source source) {
        if (sourceRegistry.contains(source)) {
            return true;
        }

        final String zkPath = this.toZkPath(this.resourceName(source));
        log.info("Register zk data change listener for path[{}].", zkPath);
        try {
            zkClient.getData()
                    .usingWatcher((CuratorWatcher) event -> {
                        final Watcher.Event.EventType eventType = event.getType();
                        // uninterested zk event
                        if (!Watcher.Event.EventType.NodeDataChanged.equals(eventType)) {
                            log.info("Uninterested zk event type: {}, and ignore it.", eventType);
                            return;
                        }

                        final String eventPath = event.getPath();
                        try {
                            if (zkPath.equals(eventPath)) {
                                log.info("Refresh zk configuration, path[{}].", eventPath);
                                ConfigInjector.getInstance().reload(source);
                            } else {
                                log.debug("[{}] unexpected change, and ignore it, path[{}].", zkPath, eventPath);
                            }
                        } catch (Throwable t) {
                            throw new ConfigException("refresh zk configuration error, path: " + eventPath, t);
                        }
                    })
                    .forPath(zkPath);
        } catch (Throwable t) {
            log.error("Try register zk data change listener error, path: {}", zkPath, t);
            return false;
        }
        sourceRegistry.add(source);
        return true;
    }
}
```

然后编写 `/META-INF/services/org.zhenchao.kratos.source.provider.SourceProvider` 文件：

```text
org.zhenchao.kratos.source.provider.ClasspathSourceProvider
org.zhenchao.kratos.source.provider.ZkSourceProvider
```

最后一步，注册 prefix 标识（不区分大小写）：

```java
ConfUtils.registerPrefix("ZK");
```

### 实现原理

Kratos 在设计和实现上主要分为两大模块：

1. 从数据源拉取配置数据，并封装成 Properties 对象；
2. 基于反射机制从 Properties 对象中获取对应的配置项并注入目标对象对应的属性上。

同时监听数据源，当数据源更新时以回调的方式更新本地配置。

整体设计图如下：

<div align=center><img  src="https://www.zhenchao.org/images/2020/kratos.png"/></div>

SourceProvider 用于从数据源加载配置数据并封装成 Properties 对象，同时注册到对应数据源的监听器以监听配置更新。ConfigInjector 会解析 options 配置，并从 Properties 中获取对应的配置项，调用类型转换器 Converter 转成目标类型，并最终注入到目标 options 中。

### 注意事项

1. 对于同一类 options 而言，不允许注册多个实例，否则会抛出 ConfigException 异常。
2. 如果希望在注入时支持系统环境变量，可以构造一个 `new PropertiesBuilderFactory(true, true)` 对象，并调用 `ConfigInjector#setBuilderFactory` 方法予以设置。
3. 方法 `ConfigInjector#reset` 会清空 ConfigInjector 管理的所有 options 实例，但是不会清空对应 options 实例已注入的属性值。
4. 方法 `ConfigManager#reset` 会在 `ConfigInjector#reset` 的基础上清空 ConfigManager 的初始化状态。
5. raw 类型是唯一的，且与一般类型互斥。
6. 不允许注入 static 类型的属性。
7. 自定义类型转换器的优先级高于系统内建的类型转换器，在实现自定义转换器时请保证代码质量。
8. 尽量认真实现 `Options#validate` 方法，对配置的正确性严格控制。
9. 被 ConfigInjector 管理的 options 实例是可能被多线程共享的，最好只允许配置库对实例进行修改。
10. 请勿在 Listener、`Options#update` 和 `Options#validate` 中实现阻塞逻辑。

### 鸣谢

设计灵感来自 [zlib-config](https://github.com/rchargel/zlib-config)，在此表示感谢。
