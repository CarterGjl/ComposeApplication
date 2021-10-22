package com.example.composeapplication.activity;

public interface TestJavaA {
    default void test() {
        System.out.println("testA");
    }
    default void testJavaA() {
        System.out.println("testJavaA");
    }
}
