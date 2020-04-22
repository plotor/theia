package org.zhenchao.kratos.example;

import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.converter.ListConverter;
import org.zhenchao.kratos.converter.SetConverter;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author zhenchao.wang 2020-04-22 16:58
 * @version 1.0.0
 */
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

    @Attribute(name = "abyte", required = false, defaultValue = "1")
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

    @Attribute(name = "list", converter = ListConverter.class)
    public List<String> list;

    @Attribute(converter = SetConverter.class)
    public Set<String> set;

    @Override
    public void update() {
        this.bigNum = this.number + this.floatingPointNumber;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
