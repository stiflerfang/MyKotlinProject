package com.stifler.kotlin

import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.KClass

fun main(args: Array<String>) {
//    val authors = listOf("Dmitry","Svetlana")
//    val readers = mutableListOf<String>()
//
//    println(readers.filter { it in authors })

//    println(listOf(1,2,3,4).penultimate)
//    printSum(listOf(1,2,3))

//    printContents(listOf("abc","bac"))

//    val strings = mutableListOf("abc","bac")
//    addAnswer(strings)
//    printContents(strings.maxBy { it.length })

//    val list:MutableList<Any?> = mutableListOf('a',1,"qwe")
//    val chars = mutableListOf<Char>('a','b','c')
//    val unknownElements: MutableList<*> = if(Random().nextBoolean())list else chars
//    unknownElements.add(42)
//    println(unknownElements.first())

//    val validators = mutableMapOf<KClass<*>,FieldValidator<*>>()
//    validators[String::class] = DefaultStringValidator
//    validators[Int::class] = DefaultIntValidator

//    validators[String::class]!!.validate("")

//    val stringValidator = validators[String::class] as FieldValidator<String>
//    println(stringValidator.validate(""))
//
//    val stringValidator1 = validators[Int::class] as FieldValidator<String>
//    println(stringValidator1.validate(""))

//    Validators.registerValidator(String::class,DefaultStringValidator)
//    Validators.registerValidator(Int::class,DefaultIntValidator)
//
//    println(Validators[String::class].validate("Kotlin"))
//    println(Validators[Int::class].validate(42))
//    println(Validators[String::class].validate(42))

}

val <T> List<T>.penultimate: T
    get() = this[size -2]

//fun <T> List<T>.filter(predicate:(T) -> Boolean):List<T>

fun printSum(c: Collection<*>){
    val intList = c as? List<Int>
            ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}

inline fun <reified T> Iterable<*>.filterIsInstance():List<T>{
    val destination = mutableListOf<T>()
    for(element in this){
        if(element is T){
            destination.add(element)
        }
    }
    return destination
}

inline fun <reified T> loadService():ServiceLoader<T>{
    return ServiceLoader.load(T::class.java)
}

fun printContents(list:List<Any>){
    println(list.joinToString())
}

fun addAnswer(list:MutableList<Any>){
    list.add(42)
}

open class Animal{
    fun feed(){
        println("feed the animal")
    }
}

class Herd<out T :Animal>{
    val animals = listOf<T>()
    val size:Int
        get() = animals.size
    operator fun get(i:Int):T{
        return animals.get(i)
    }
}

fun feedAll(animals:Herd<Animal>){
    for(i in 0 until animals.size){
        animals[i].feed()
    }
}

class Cat: Animal(){
    fun cleanLitter(){
        println("Cat clean litter")
    }
}

fun takeCareOfCats(cats:Herd<Cat>){
    for(i in 0 until cats.size){
        cats[i].cleanLitter()
        feedAll(cats)
        feedAll(cats as Herd<Animal>)
    }
}

interface FieldValidator<in T>{
    fun validate(input:T):Boolean
}

object DefaultStringValidator:FieldValidator<String>{
    override fun validate(input: String): Boolean = input.isNotEmpty()
}

object DefaultIntValidator:FieldValidator<Int>{
    override fun validate(input: Int): Boolean = input>= 0
}

object Validators{
    val validators = mutableMapOf<KClass<*>,FieldValidator<*>>()

    fun <T:Any> registerValidator(kClass:KClass<T>,fieldValidator: FieldValidator<T>){
        validators[kClass] = fieldValidator
    }

    operator fun <T:Any> get(kClass:KClass<T>):FieldValidator<T> =
            validators[kClass] as? FieldValidator<T> ?: throw IllegalArgumentException("No validator for ${kClass.simpleName}")
}