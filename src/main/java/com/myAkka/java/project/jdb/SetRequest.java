package com.myAkka.java.project.jdb;

import java.io.Serializable;

public class SetRequest implements Serializable{

    private static final long serialVersionUID = -1631031754430894753L;
    public final String key;
    public final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
