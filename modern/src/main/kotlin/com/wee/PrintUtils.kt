package com.wee

/**
 *@author young
 *@create 22/4/5 12:30
 */
fun printMemoryInfo(str: String = "") {
    val runtime = Runtime.getRuntime()
    val totalMemory = runtime.totalMemory() / 1024 / 1024
    val freeMemory = runtime.freeMemory() / 1024 / 1024
    val maxMemory = runtime.maxMemory() / 1024 / 1024
    val usedMemory = totalMemory - freeMemory
    println("$str  total:$totalMemory,free:$freeMemory,max:$maxMemory,used:$usedMemory")
}