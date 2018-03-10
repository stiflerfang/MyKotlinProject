package com.stifler.kotlin

import sun.util.resources.cldr.lag.LocaleNames_lag
import java.util.concurrent.locks.Lock

fun main(args: Array<String>) {
//    val sum = {x:Int,y:Int -> x+y}
//    val action = { println(42)}
//
//    val sum1:(Int,Int) -> Int = {x:Int,y:Int -> x+y}
//    val action1:() -> Unit = { println(42)}

//    twoAndThree{a,b -> a+b}
//    twoAndThree{a,b -> a*b}

//    println("ab1c".filter { it in 'a'..'z' })

//    val letters = listOf("Alpha","Beta")
//    println(letters.joinToString())
//    println(letters.joinToString { it.toLowerCase() })
//    println(letters.joinToString(separator = "! ",prefix = "!",postfix = "!",transform = {it.toUpperCase()}))

//    val calculator = getShippingCostCaluculator(Delivery.EXPEDITED)
//    println("Shipping costs ${calculator(Order(3))}")

//    val contacts = listOf(PersonOfSession8("Dmitry","Jemerov","123-4567"),
//            PersonOfSession8("Svetlana","Isakova",null))
//    val contactListFilters = ContactListFilters()
//    with(contactListFilters){
//        prefix = "Dm"
//        onlyWithPhoneNumber = true
//    }
//    println(contacts.filter(contactListFilters.getPredicate()))

//    val log = listOf(SiteVisit("/",34.0, OS.WINDOWS),
//            SiteVisit("/",22.0, OS.MAC),
//            SiteVisit("/login",12.0, OS.WINDOWS),
//            SiteVisit("/signup",8.0, OS.IOS),
//            SiteVisit("/",16.3, OS.ANDROID),
//            SiteVisit("/register",14.0, OS.LINUX))
//
//    val averageWindowsDuration = log
//            .filter { it.os == OS.WINDOWS }
//            .map(SiteVisit::duration)
//            .average()
//
//    val averageMobileDuration = log
//            .filter { it.os in setOf(OS.IOS,OS.ANDROID) }
//            .map(SiteVisit::duration)
//            .average()
//
//    println(averageWindowsDuration)
//    println(averageMobileDuration)
//    println(log.averageDurationFor(OS.WINDOWS))
//    println(log.averageDurationFor(OS.MAC))
//    println(log.averageDurationFor1{it.os in setOf(OS.IOS,OS.ANDROID)})
//    println(log.averageDurationFor1{it.os == OS.IOS && it.path == "/signup"})

    val people = listOf(Person("Alice",29,1000),Person("Bob",31,200))

    lookForAlice(people)
    println("**********")
    lookForAlice1(people)
    println("**********")
    lookForAlice2(people)
    println("**********")
}

fun twoAndThree(operation:(Int,Int) -> Int){
    val result = operation(2,3)
    println("The result is $result")
}

fun String.filter(predicate:(Char) -> Boolean):String{
    val sb = StringBuffer()
    for(index in 0 until length){
        val element = get(index)
        if(predicate(element)){
            sb.append(element)
        }
    }
    return sb.toString()
}

fun <T> Collection<T>.joinToString(separator:String = ", ",prefix:String = "",postfix:String = "",
                                   transform:(T) -> String = {it.toString()}):String{
    val result = StringBuffer(prefix)

    for((index, element) in this.withIndex()){
        if(index > 0){
            result.append(separator)
        }
        result.append(transform(element))
    }

    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.joinToString1(separator:String = ", ",prefix:String = "",postfix:String = "",
                                   transform:((T) -> String)? = null):String{
    val result = StringBuffer(prefix)

    for((index, element) in this.withIndex()){
        if(index > 0){
            result.append(separator)
        }
        val str = transform?.invoke(element)?:element.toString()
        result.append(str)
    }

    result.append(postfix)
    return result.toString()
}

enum class Delivery{
    STANDARD, EXPEDITED
}

class Order(val itemCount: Int)

fun getShippingCostCaluculator(delivery: Delivery):(Order) -> Double{
    if(delivery == Delivery.EXPEDITED){
        return {order ->  6 + 2.1 * order.itemCount}
    }

    return {order -> 1.2 * order.itemCount }
}

class ContactListFilters{
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate():(PersonOfSession8) -> Boolean{
        val startsWithPrefix = {p: PersonOfSession8 ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if(!onlyWithPhoneNumber){
            return startsWithPrefix
        }
        return{
            startsWithPrefix(it) && it.phoneNumber != null
        }
    }
}

data class PersonOfSession8(val firstName: String, val lastName: String, val phoneNumber:String?)

data class SiteVisit(val path: String, val duration: Double, val os: OS)

fun List<SiteVisit>.averageDurationFor(os: OS) = filter { it.os == os }.map(SiteVisit::duration).average()
fun List<SiteVisit>.averageDurationFor1(predicate:(SiteVisit) -> Boolean) = filter(predicate).map(SiteVisit::duration).average()

enum class OS{ WINDOWS, LINUX, MAC, IOS, ANDROID}

inline fun <T> synchronized(lock: Lock, action:() -> T):T{
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

fun foo(l: Lock){
    println("Before sync")
    synchronized(l){
        println("Action")
    }
    println("After sync")
}

class LockOwner(val lock: Lock){
    fun runUnderLock(body:() -> Unit){
        synchronized(lock,body)
    }
}

fun lookForAlice(person: List<Person>){
    person.forEach{
        if(it.name == "Alice"){
            println("Found!")
            return
//            return@forEach
        }
    }
    println("Alice is not found")
}

fun lookForAlice1(person: List<Person>){
    person.forEach label@ {
        if (it.name == "Alice") {
            println("Found!")
            return@label
        }
    }
    println("Alice might be somewhere")
}

fun lookForAlice2(people: List<Person>){
    people.forEach(fun(person){
        if(person.name == "Alice"){
            println("Found!")
            return
        }
        println("${person.name} is not Alice")
    })
}