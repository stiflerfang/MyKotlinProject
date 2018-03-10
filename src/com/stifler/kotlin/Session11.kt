package com.stifler.kotlin

import java.time.LocalDate
import java.time.Period

fun main(args: Array<String>) {
//    val yesterday = 1.days.ago
//    val bavarianGreeter = Greeter("Servus")
//    bavarianGreeter("Dmitry")

//    val i1 = Issue("IDEA-154446", "IDEA", "Bug", "Major",
//            "Save setting failed")
//    val i2 = Issue("KT-12183", "Kotlin", "Feature", "Normal",
//            "Intention: convert serveral calls on the same receiver to with/apply")
//
//    val predicate = ImportantIssuesPredicate("IDEA")
//    for(issue in listOf(i1,i2).filter(predicate)){
//        println(issue.id)
//    }

//    val dependencies = DependencyHandler()
//
//    dependencies.compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")
//
//    dependencies{
//        compile("org.jetbrains.kotlin:kotlin-reflect:1.0.0")
//    }

//    val s = "kotlin is good"
//    s should startWith("kot")
//    "kotlin" should start with("kot")
//    "kotlin" should end with("lin")

    println(1.days.now)
    println(1.days.ago)
    println(1.days.fromNow)
}

//fun createSimpleTable() = createHTML().table{
//    tr{
//        td{
//            +"cell"
//        }
//    }
//}

class Greeter(val greeting: String) {
    operator fun invoke(name: String) {
        println("$greeting, $name!")
    }
}

data class Issue(val id: String, val project: String, val type: String,
                 val priority: String, val description: String)

class ImportantIssuesPredicate(val project: String): (Issue) -> Boolean {
    override fun invoke(issue: Issue): Boolean {
        return issue.project == project && issue.isImportant()
    }

    private fun Issue.isImportant(): Boolean {
        return type == "Bug" && (priority == "Major" || priority == "Critical")
    }
}

class DependencyHandler{
    fun compile(coordinate:String){
        println("Added dependency on $coordinate")
    }

    operator fun invoke(body:DependencyHandler.() -> Unit){
        body()
    }
}

infix fun <T> T.should(matcher: Matcher<T>) = matcher.test(this)

interface Matcher<T>{
    fun test(value: T)
}

class startWith(val prefix: String): Matcher<String>{
    override fun test(value: String) {
        if(!value.startsWith(prefix)){
            throw AssertionError("String $value does not start with $prefix")
        }
    }
}

object start

infix fun String.should(x: start):StartWrapper = StartWrapper(this)

class StartWrapper(val value: String){
    infix fun with(prefix: String) =
            if(!value.startsWith(prefix)){
                throw AssertionError("String does not start with $prefix :$value")
            }else{

            }
}

object end

infix fun String.should(x:end):EndWrapper = EndWrapper(this)

class EndWrapper(val value: String){
    infix fun with(postfix: String) =
            if(!value.endsWith(postfix)){
                throw AssertionError("String does not end with $postfix :$value")
            }else{

            }
}

val Int.days: Period
    get() = Period.ofDays(this)

val Period.now:LocalDate
    get() = LocalDate.now()

val Period.ago: LocalDate
    get() = LocalDate.now() - this

val Period.fromNow:LocalDate
    get() = LocalDate.now() + this