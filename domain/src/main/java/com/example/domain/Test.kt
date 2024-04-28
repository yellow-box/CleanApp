package com.example.domain

class Test {
    fun ook(){}
}

fun check(seq:Sequence<Int>):Int?{
    for(ele in seq) {
        if(ele > 15){
            return ele
        }
    }
    return null
}
fun main(){
    val list = listOf(9,20,7,6,4)
    val seq = list.asSequence()
    println("find ${check(seq)?:-1}")
    println("done!")
}
