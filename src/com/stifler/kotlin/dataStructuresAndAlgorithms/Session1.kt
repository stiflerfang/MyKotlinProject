package com.stifler.kotlin.dataStructuresAndAlgorithms

fun main(args: Array<String>) {
//    println(fact(5))
    towersOfHanoi(3,'A','B','C')
}

fun fact(n: Int): Int{
    return when(n){
        1 -> 1
        0 -> 1
        else -> n * fact(n-1)
    }
//    if(n == 1){
//        return 1
//    }else if(n == 0){
//        return 1
//    }else{
//        return n * fact(n-1)
//    }
}

fun towersOfHanoi(n: Int,frompeg:Char,topeg: Char,auxpeg: Char){
    //如果仅有一个圆盘，直接移动，然后返回
    if(n == 1){
        println("Move disk 1 from peg " + frompeg + " to peg " + topeg)
        return
    }
    //利用C柱做辅助，将A柱最上面的n-1个圆盘移动到B柱
    towersOfHanoi(n-1,frompeg, auxpeg, topeg)
    //将余下的圆盘从A柱移动C柱
    println("Move disk from peg " + frompeg + " to peg " + topeg)
    //利用A柱作为辅助，将B柱上的n-1个圆盘移动到C柱
    towersOfHanoi(n-1,auxpeg,topeg,frompeg)
}