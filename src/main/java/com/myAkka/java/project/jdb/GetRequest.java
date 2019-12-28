package com.myAkka.java.project.jdb;

import java.io.Serializable;

public class GetRequest implements Serializable{
    private static final long serialVersionUID = -4779541780294993579L;
    public final String key;

    public GetRequest(String key) {
        this.key = key;
    }
}
