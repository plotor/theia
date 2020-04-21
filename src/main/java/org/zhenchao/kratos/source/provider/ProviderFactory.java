package org.zhenchao.kratos.source.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.source.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * {@link SourceProvider} factory.
 *
 * @author zhenchao.wang 2016-09-06 11:39:06
 * @version 1.0.0
 */
public class ProviderFactory {

    private static final Logger log = LoggerFactory.getLogger(ProviderFactory.class);

    private static final ProviderFactory INSTANCE = new ProviderFactory();

    private final Map<Source, SourceProvider> providerMap = new HashMap<>();
    private final List<SourceProvider> providers = new ArrayList<>();

    private ProviderFactory() {
        final ServiceLoader<SourceProvider> serviceLoader =
                ServiceLoader.load(SourceProvider.class, Thread.currentThread().getContextClassLoader());
        serviceLoader.reload();
        for (final SourceProvider provider : serviceLoader) {
            log.info("Loading and initialize source provider: {}.", provider.getClass());
            provider.onInitialize();
            providers.add(provider);
        }
        Collections.sort(providers);
    }

    public static ProviderFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get matched {@link SourceProvider} for {@code source}.
     *
     * @param source
     * @return {@code null} is no provider was founded.
     */
    public SourceProvider getSourceProvider(final Source source) {
        final SourceProvider provider = providerMap.get(source);
        if (null != provider) {
            return provider;
        }
        final Optional<SourceProvider> opt = providers.stream().filter(p -> p.support(source)).findFirst();
        return opt.orElse(null);
    }

    /**
     * Clean up.
     */
    public void clear() {
        for (final SourceProvider provider : providerMap.values()) {
            if (providers.contains(provider)) {
                provider.onDestroy();
            }
        }
        providers.clear();
        providerMap.clear();
    }

}
