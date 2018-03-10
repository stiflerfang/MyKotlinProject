package com.stifler.kotlin

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.time.LocalDate
import kotlin.math.max
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
//    println("hello world")
//    (0..18).forEach { print(it)}
//    println("")
//    println((0..18).joinToString(separator = ";"))
//    println((0..18).joinToString { it.toString()+"*" })
//    println((0..18).joinToString(separator = ";",prefix = "[",postfix = "]",
//            limit = 10,truncated = "!!!",transform = {it.toString()+"*"}))

//    val newYear = LocalDate.ofYearDay(2018,1)
//    val daysOff = newYear.minusDays(1)..newYear.plusDays(1 )
//
//    for(dayOff in daysOff){
//        println(dayOff)
//    }

//    val(name, ext) = splitFileName("example.kt")
//    println(name+";"+ext)

//    val map = mapOf("Oracle" to "Java","JetBrains" to "Kotlin")
//    printEntries(map)

//    val p = Person("Dmitry",34,2000)
//    p.addPropertyChangeListener(PropertyChangeListener { event ->
//        println("Property ${event.propertyName} changed," + "from ${event.oldValue} to ${event.newValue}")
//    })
//    p.age = 35
//    p.salary = 2100

//    val p1 = Person("Dmitry",34,2000)
//    p1.addPropertyChangeListener(PropertyChangeListener { event ->
//        println("Property ${event.propertyName} changed," + "from ${event.oldValue} to ${event.newValue}")
//    })
//    p1.age = 35
//    p1.salary = 2100

//    val p2 = Person("Dmitry",34,2000)
//    p2.addPropertyChangeListener(PropertyChangeListener { event ->
//        println("Property ${event.propertyName} changed," + "from ${event.oldValue} to ${event.newValue}")
//    })
//    p2.age = 35
//    p2.salary = 2100

//    val p = Person4()
//    val data = mapOf("name" to "Dmitry","company" to "JetBrains")
//    for((attrName,value) in data){
//        p.setAttribute(attrName,value)
//    }
//    println(p.name)

//    val p = Person5()
//    val data = mapOf("name" to "Dmitry","company" to "JetBrains")
//    for((attrName,value) in data){
//        p.setAttribute(attrName,value)
//    }
//    println(p.name)

//    printAllStatusCombination(2,6)

//    val list = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
//
//    val index = binarySearch(list, 11);
//    println("index is ${index},value is ${list[index]}")
//    val index1 = binarySearch(list, 21);
//    println("index is ${index1},${index1.inv()}")
//    val index11 = binarySearch(list, -1);
//    println("index is ${index11},${index11.inv()}")
//    val index2 = binarySearch(list, 20);
//    println("index is ${index2},value is ${list[index2]}")

//    println(doTime(200, intArrayOf(12,11,13,20,50),40000,8))
//    println(doTime(200, intArrayOf(1,1,1,10,10),5000,10))

    val list = intArrayOf(25,12,4,53,23,6,8,45,14,32,9,76,80)
    quickSort(list,0,list.size-1)
    println(list.joinToString())

}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
        object : Iterator<LocalDate> {
            var current = start

            override fun hasNext(): Boolean {
                return current <= endInclusive //To change body of created functions use File | Settings | File Templates.
            }

            override fun next() = current.apply {
                current = plusDays(1)
            }
        }

data class NameComponents(val name: String, val extension: String)

fun splitFileName(fullName: String): NameComponents {
    val result = fullName.split(".", limit = 2)
    return NameComponents(result[0], result[1])
}

fun splitFileName1(fullName: String): NameComponents {
    val (name, extension) = fullName.split(".", limit = 2)
    return NameComponents(name, extension)
}

fun printEntries(map: Map<String, String>) {
    for ((key, value) in map) {
        println("$key -> $value")
    }
}

open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

class Person(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    var age: Int = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("age", oldValue, newValue)
        }
    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("salary", oldValue, newValue)
        }
}

class ObservableProperty(val propName: String, var propValue: Int, val changeSupport: PropertyChangeSupport) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}

class ObservableProperty1(var propValue: Int, val changeSupport: PropertyChangeSupport) {
    operator fun getValue(p: Person2, prop: KProperty<*>): Int = propValue

    operator fun setValue(p: Person2, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class Person1(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(newValue) {
            _age.setValue(newValue)
        }

    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(newValue) {
            _salary.setValue(newValue)
        }
}

class Person2(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    var age: Int by ObservableProperty1(age, changeSupport)
    var salary: Int by ObservableProperty1(salary, changeSupport)
}

class Person3(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    private val observer = { prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }

    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)
}

class Person4 {
    private val _attributes = hashMapOf<String, String>()

    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }

    val name: String
        get() = _attributes["name"]!!
}

class Person5 {
    private val _attributes = hashMapOf<String, String>()

    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }

    val name: String by _attributes
}

fun printAllStatusCombination(status: Int, amount: Int) {
//    val yes = "√"
//    val no = "×"
//    val array = Array(6,{yes})
//    for (s1 in array) {
//        println(s1)//--->>默认值 默认值。。。。默认值
//    }
//    println(array.joinToString())
    var sb = StringBuffer()
    var list = mutableListOf<String>()
    printSingleStauts(status, amount, sb, list)
    for (string in list) {
        println(string.toCharArray().joinToString(separator = "\t"))
    }
}

fun printSingleStauts(status: Int, amount: Int, sb: StringBuffer, list: MutableList<String>) {
    if (amount == 1) {
        for (i in 1..status) {
            sb.append(i.toString())
//            println("###"+sb.toString().replace("1","√").replace("2","×"))
//            println("###"+sb.toString())
            list.add(sb.toString().replace("1", "√").replace("2", "×"))
            sb.delete(sb.length - 1, sb.length)
//            println("$$$"+sb.toString())
//            println("$$$"+sb.toString().replace("1","√").replace("2","×"))
        }
//        sb.delete(sb.length-1,sb.length)
        return
    }

    for (i in 1..status) {
        sb.append(i.toString())
        println("!!!" + sb.toString())
        printSingleStauts(status, amount - 1, StringBuffer(sb.toString()), list)
        sb.delete(sb.length - 1, sb.length)
    }
}

fun binarySearch(array: IntArray, value: Int): Int {
    array.sort()
    var low = 0
    var high = array.size - 1

    while (low <= high) {
        val middle = low + high ushr 1
        val middleValue = array[middle]

        if (middleValue < value) {
            low = middle + 1
        } else if (middleValue > value) {
            high = middle - 1
        } else {
            return middle
        }
    }
    return low.inv()
}

fun doTime(maxQps: Int,rtList: IntArray,requestNum: Int, threadNum: Int): Int{
    var qpsSum: Int = 0
    for(rt in rtList){
        val singleQps = threadNum * 1000 / rt
        if(singleQps > maxQps){
            qpsSum += maxQps
        }else{
            qpsSum += singleQps
        }
    }

    return requestNum * 1000 / qpsSum
}

fun quickSort(array: IntArray,low: Int, high: Int){
    var pivot: Int

    if(high > low){
        pivot = partition(array,low,high)
        println(array.joinToString())
        quickSort(array,low,pivot-1)
        quickSort(array,pivot+1,high)
    }
}

fun partition(array: IntArray,low: Int, high: Int): Int{
    var left:Int
    var right:Int
    var pivot_item:Int = array[low]

    left = low
    right = high

    while (left < right){
        while (array[left] <= pivot_item){
            left++
        }
        while (array[right] > pivot_item){
            right--
        }
        if(left < right){
            var temp = array[left]
            array[left] = array[right]
            array[right] = temp
        }
        println("###"+array.joinToString())
    }
    array[low] = array[right]
    array[right] = pivot_item
    return right
}