package com.myAkka.java.project.jdb;

import java.io.Serializable;

public class KeyNotFoundException extends Exception implements Serializable{
    private static final long serialVersionUID = 6297038611005660171L;
    public final String key;
    public KeyNotFoundException(String key) {
        this.key = key; }
}
