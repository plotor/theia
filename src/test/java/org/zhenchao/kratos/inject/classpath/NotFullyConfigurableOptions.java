package org.zhenchao.kratos.inject.classpath;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;

import java.io.File;

@Configurable(Constants.CP_PREFIX + "configurable_options")
public class NotFullyConfigurableOptions extends AbstractOptions {

    private static final long serialVersionUID = -6059970408628587423L;

    @Attribute(name = "myFiles")
    private File[] files;

    private int number;

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

    public float anotherFloat;

    private char achar;

    private byte abyte;

    private String helloWorld;

    private double bigNum = 0;

    public File[] getFiles() {
        return this.files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Attribute
    public Double getFloatingPointNumber() {
        return this.floatingPointNumber;
    }

    public String getMessage() {
        return this.helloWorld;
    }

    public String getPropMessage() {
        return this.propMessage;
    }

    public void setAnotherLongValue(long anotherLongValue) {
        this.anotherLongValue = anotherLongValue;
    }

    @Attribute
    public void setName(String name) {
        this.helloWorld = "Hello " + name + "!";
    }

    public String getFieldMessage() {
        return this.fieldMessage;
    }

    @Attribute
    public char getAchar() {
        return this.achar;
    }

    public void setAchar(char achar) {
        this.achar = achar;
    }

    @Attribute(name = "oneMoreFloat")
    public void setAnotherFloat(float anotherFloat) {
        this.anotherFloat = anotherFloat;
    }

    public byte getMyByte() {
        return this.abyte;
    }

    @Attribute(name = "abyte", defaultValue = "1")
    public void setMyByte(byte aByte) {
        this.abyte = aByte;
    }

    public double getBigNum() {
        return this.bigNum;
    }

    public void setTrueFalse(Boolean trueFalse) {
        this.trueFalse = trueFalse;
    }

    public Boolean getTrueFalse() {
        return this.trueFalse;
    }

}
