package com.example.composeapplication.activity;

import com.example.composeapplication.activity.bsae.Demo;

public class TestJava implements TestJavaA,TestJavaB {

    private String a;
    @Override
    public void test() {


    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
