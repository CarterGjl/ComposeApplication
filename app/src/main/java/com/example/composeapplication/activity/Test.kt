package com.example.composeapplication.activity

interface TestA {
    fun test(){
        println("A")
    }
    fun testA(): Unit {
        println("testA")
    }
}

interface TestB {
    fun test(){
        println("B")
    }
    fun testB(): Unit {
        println("testB")
    }
}

class Test : TestA,TestB{
    override fun test() {
        println("test")
    }

}

fun makeAdder(addBy: Int): (i: Int) -> Int {
    return fun(i: Int): Int {
        return addBy + i
    }
}
fun main() {
    val add = makeAdder(1)
    println(add(3))
    println(add(4))
}