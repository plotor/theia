package org.zhenchao.kratos.source.provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.source.PropertiesBuilder;
import org.zhenchao.kratos.source.Source;
import org.zhenchao.kratos.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhenchao.wang 2016-09-06 13:25:20
 * @version 1.0.0
 */
public abstract class AbstractSourceProvider implements SourceProvider {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onInitialize() {
    }

    @Override
    public Properties loadProperties(
            final Source source, final PropertiesBuilder builder) throws ConfigException {
        try {
            this.preLoad(source);
            return this.doLoadProperties(source, builder);
        } finally {
            this.postLoad(source);
        }
    }

    /**
     * Load configuration from source, and convert to {@link Properties}.
     *
     * @param source
     * @param builder
     * @return
     * @throws ConfigException
     */
    protected abstract Properties doLoadProperties(final Source source, final PropertiesBuilder builder) throws ConfigException;

    @Override
    public void onDestroy() {
    }

    @Override
    public int compareTo(SourceProvider that) {
        return Integer.compare(that.priority(), this.priority());
    }

    protected void preLoad(final Source source) {
        if (log.isDebugEnabled()) {
            log.debug("Pre load, provider[{}], optionsClass[{}], resource[{}].",
                    this.getClass(), source.getOptionsClass(), source.getResourceName());
        }
    }

    protected void postLoad(Source source) {
        if (log.isDebugEnabled()) {
            log.debug("Post load, provider[{}], optionsClass[{}], resource[{}].",
                    this.getClass(), source.getOptionsClass(), source.getResourceName());
        }
    }

    protected String resourceName(final Source source) {
        return source.getResourceName();
    }

    protected Properties toProperties(final String data) throws ConfigException {
        Properties properties = new Properties();
        try (final InputStream inputStream = new ByteArrayInputStream(Bytes.toBytes(data))) {
            try {
                log.debug("Load properties from input stream:\n {}", data);
                properties.load(inputStream);
            } catch (Throwable t) {
                // ignore
                properties.clear();
            }
            properties.put(Constants.RAW_CONFIG_KEY, StringUtils.trimToEmpty(data));
        } catch (Throwable t) {
            throw new ConfigException("parse string text to properties error: \n" + data, t);
        }
        return properties;
    }

    /*protected InputStream[] copyStream(final InputStream inputStream, int copies) throws IOException {
        Assert.isTrue(null != inputStream, "null input stream");
        Assert.isTrue(copies > 0, "invalid number of copies: " + copies);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            IOUtils.copy(inputStream, baos);
            InputStream[] iss = new InputStream[copies];
            for (int i = 0; i < copies; i++) {
                iss[i] = new ByteArrayInputStream(baos.toByteArray());
            }
            return iss;
        } finally {
            inputStream.close();
        }
    }

    protected void closeInputStreams(final InputStream[] streams) {
        if (null == streams) {
            return;
        }
        for (final InputStream stream : streams) {
            IOUtils.closeQuietly(stream);
        }
    }*/

}
