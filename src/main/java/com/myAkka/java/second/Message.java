package com.myAkka.java.second;

import java.util.Collections;
import java.util.List;

public class Message {
    private final int age;
    private final List<String> list;

    public Message(int age, List<String> list) {
        this.age = age;
        this.list = Collections.unmodifiableList(list);
    }

    public int getAge() {
        return age;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "Message{" +
                "age=" + age +
                ", list=" + list +
                '}';
    }
}
