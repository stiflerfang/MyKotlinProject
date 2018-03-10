package com.stifler.kotlin

import ru.yole.jkid.JsonExclude
import ru.yole.jkid.JsonName
import ru.yole.jkid.joinToStringBuilder
import ru.yole.jkid.serialization.*
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

var count = 0

fun main(args: Array<String>) {
    val json = """{"name":"Alice","age":29}"""

    val person = PersonForSesson10("Alice",29)
    val kClass = person.javaClass.kotlin
    println(kClass.simpleName)
    kClass.memberProperties.forEach { println(it.name) }

    val kFunction = ::fooMe
    kFunction.call(42)

    val kFunction1 = ::sum
    println(kFunction1.invoke(1,2) + kFunction1(3,4))

    var counter = 0
    val kProperty1 = counter
    val kProperty = ::count
    kProperty.setter.call(21)
    println(kProperty.get())

    val memberProperty = PersonForSesson10::age
    println(memberProperty.get(person))
}

data class PersonForSesson10(val name:String,val age:Int)

fun fooMe(x:Int) = println(x)

fun sum(x:Int,y:Int) = x + y

fun serialize(obj:Any):String = buildString { serializeObject(obj) }

inline fun <reified T> KAnnotatedElement.findAnnotation():T?=
        annotations.filterIsInstance<T>().firstOrNull()

private fun StringBuilder.serializeObject(obj:Any){
    obj.javaClass.kotlin.memberProperties
            .filter { it.findAnnotation<JsonExclude>() == null }
            .joinToStringBuilder(this,prefix = "{",postfix="}"){
                serializeProperty(it,obj)
            }
}

private fun StringBuilder.serializeProperty(prop:KProperty1<Any,*>,obj:Any){
    val jsonNameAnn = prop.findAnnotation<JsonName>()
    val propName = jsonNameAnn?.name?:prop.name
    serializeString(propName)
    append(": ")
    serializePropertyValue(prop.get(obj))
}

private fun StringBuilder.serializeString(s: String) {
    append('\"')
    s.forEach { append(it.escape()) }
    append('\"')
}

private fun StringBuilder.serializePropertyValue(value: Any?) {
    when (value) {
        null -> append("null")
        is String -> serializeString(value)
        is Number, is Boolean -> append(value.toString())
        is List<*> -> serializeList(value)
        else -> serializeObject(value)
    }
}

private fun Char.escape(): Any =
        when (this) {
            '\\' -> "\\\\"
            '\"' -> "\\\""
            '\b' -> "\\b"
            '\u000C' -> "\\f"
            '\n' -> "\\n"
            '\r' -> "\\r"
            '\t' -> "\\t"
            else -> this
        }

private fun StringBuilder.serializeList(data: List<Any?>) {
    data.joinToStringBuilder(this, prefix = "[", postfix = "]") {
        serializePropertyValue(it)
    }
}