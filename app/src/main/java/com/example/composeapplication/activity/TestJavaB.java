package com.example.composeapplication.activity;

public interface TestJavaB {
    default void test(){
        System.out.println("testB");
    }
    default void testJavaB(){
        System.out.println("testJavaB");
    }
}
