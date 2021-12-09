package com.example.composeapplication.activity.bsae

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}


val <T> List<T>.lastIndex: Int
    get() = size - 1

data class Person(var name: String, var age: Int)
fun main() {
    arrayListOf<String>().lastIndex
    val p = Person(name = "lilei",age = 18)
    val (name,age) = p
    println(age)
}

class Demo{
    companion object {
        // 使用JvmStatic 完成 java 调用 kotlin
        @JvmStatic
        fun test() {

        }
        var title:String? = null
    }
}