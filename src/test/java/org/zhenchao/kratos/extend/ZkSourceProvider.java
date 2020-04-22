package org.zhenchao.kratos.extend;

import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.source.PropertiesBuilder;
import org.zhenchao.kratos.source.Source;
import org.zhenchao.kratos.source.provider.AbstractSourceProvider;

import java.util.Properties;

/**
 * @author zhenchao.wang 2020-04-22 17:51
 * @version 1.0.0
 */
public class ZkSourceProvider extends AbstractSourceProvider {

    @Override
    protected Properties doLoadProperties(Source source, PropertiesBuilder builder) throws ConfigException {
        return null;
    }

    @Override
    public boolean support(Source source) {
        return false;
    }

    @Override
    public int priority() {
        return 0;
    }
}
