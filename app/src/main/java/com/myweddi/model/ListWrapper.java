package com.myweddi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListWrapper<T> {
    private List<T> list;

    public ListWrapper(List<T> list) {
        this.list = list;
    }

    public ListWrapper() {
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
