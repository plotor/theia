package org.zhenchao.kratos.source;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.zhenchao.kratos.BaseTestCase;
import org.zhenchao.kratos.source.provider.ProviderFactory;

public class SourceProviderFactoryTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        assertNotNull(ProviderFactory.getInstance());
        assertSame(ProviderFactory.getInstance(), ProviderFactory.getInstance());
    }

}
