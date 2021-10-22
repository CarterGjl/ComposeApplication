package com.example.composeapplication.activity.bsae

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.WindowCompat

open class BaseActivity : AppCompatActivity() {

    companion object{

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


    }




}
fun enableFlags(bold: Boolean = false, hidden: Boolean = false): Unit {

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
        fun test(): Unit {

        }
        var title:String? = null
    }
}