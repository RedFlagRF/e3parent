package com.e3.commons.pojo;


import java.io.Serializable;

/**
 * @Author RedFlag
 * @Description easyui树形结构
 * @Date 16:37 2019/1/24
 **/

public class EasyUiTreeNode implements Serializable {
    private long id;
    public String text;
    public String state ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
