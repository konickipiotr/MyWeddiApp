package com.myweddi.module.table.model;

import java.util.Map;
import java.util.Set;

public class DiVal {
    private Long id;
    private String name;

    public DiVal() {
    }

    public DiVal(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(name == null)
            return "EMPTY";
        return name;
    }
}
