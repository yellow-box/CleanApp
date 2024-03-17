package com.example.domain.base

import java.io.PrintWriter
import java.io.StringWriter
import kotlin.Exception

class Util {
}

fun printlnCallStack(tag: String) = try {
    throw Exception(tag)
} catch (e: Exception) {
    val stringWriter = StringWriter()
    e.printStackTrace(PrintWriter(stringWriter))
    println("$tag, stack=${stringWriter}")
}

fun printExceptionCallStack(tag: String, e: Exception) {
    val stringWriter = StringWriter()
    e.printStackTrace(PrintWriter(stringWriter))
    println("$tag, stack=${stringWriter}")
}