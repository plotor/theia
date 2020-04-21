package org.zhenchao.kratos.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.BaseTestCase;
import org.zhenchao.kratos.ConfigInjector;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.Options;

@Configurable("GConf:PASSPORT.TEST:example")
public class SourceTest extends BaseTestCase implements Options {

    private static final long serialVersionUID = -2966109051323512806L;

    @Test
    public void testHashCode() {
        Source s1 = new Source(this.getClass(), "GConf:PASSPORT.TEST:example");
        Source s2 = new Source(this.getClass(), "GConf:PASSPORT.TEST:example");
        Source s3 = new Source(this.getClass(), "ZK:PASSPORT.TEST:example");
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s2.hashCode(), s3.hashCode());
    }

    @Test
    public void testEqualsObject() {
        Source s1 = new Source(this.getClass(), "GConf:PASSPORT.TEST:example");
        Source s2 = new Source(this.getClass(), "GConf:PASSPORT.TEST:example");
        Source s3 = new Source(this.getClass(), "ZK:PASSPORT.TEST:example");

        assertEquals(s1, s1);
        assertEquals(s1, s2);
        assertEquals(s2, s1);
        assertNotSame(s1, s2);
        assertNotEquals(s1, s3);
        assertNotEquals(s3, s1);
        assertNotNull(s3);
        assertNotEquals(s3, ConfigInjector.getInstance());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingConfigurableAnnotation() {
        new Source(new NoAnnotationOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingResource() {
        new Source(new NoResourceOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidResource() {
        new Source(new InvalidResourceOptions());
    }

    @Test
    public void testConfigurableSourceObject() {
        Source s1 = new Source(new GConfResourceOptions());
        assertSame(GConfResourceOptions.class, s1.getOptionsClass());
        assertEquals("GConf:PASSPORT.TEST:example", s1.getResourceName());

        Source s2 = new Source(new ZkResourceOptions());
        assertSame(ZkResourceOptions.class, s2.getOptionsClass());
        assertEquals("ZK:PASSPORT.TEST:example", s2.getResourceName());
    }

    @Test
    public void testSource() throws Exception {
        Source source = new Source(this.getClass(), "ZK:PASSPORT.TEST:example");
        assertSame(this.getClass(), source.getOptionsClass());
        assertEquals("ZK:PASSPORT.TEST:example", source.getResourceName());
    }

    @Test(expected = IllegalStateException.class)
    public void testSourceWithNull() {
        new Source(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSourceClassOfQStringNull() {
        new Source(this.getClass(), null);
    }

    public static class NoAnnotationOptions extends AbstractOptions {

        private static final long serialVersionUID = -7790158272873738009L;
    }

    @Configurable
    public static class NoResourceOptions extends AbstractOptions {

        private static final long serialVersionUID = 4475281794288292986L;
    }

    @Configurable(resource = "PASSPORT.TEST:example")
    public static class InvalidResourceOptions extends AbstractOptions {

        private static final long serialVersionUID = 3641595007082436680L;
    }

    @Configurable("GConf:PASSPORT.TEST:example")
    public static class GConfResourceOptions extends AbstractOptions {

        private static final long serialVersionUID = -2570195653697520971L;
    }

    @Configurable(resource = "ZK:PASSPORT.TEST:example")
    public static class ZkResourceOptions extends AbstractOptions {

        private static final long serialVersionUID = -4683158010978387790L;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean validate() {
        return true;
    }
}
