package com.example.libannotation.basic

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.FileSpec

open class MEnv(val resolver: Resolver, val codeGenerator: CodeGenerator, val logger: KSPLogger) {
}