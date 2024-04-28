package com.example.libannotation.basic

abstract class MProcessStep<T : MElement,R>(protected val env: MEnv) {
    abstract val annotationClassName: String
    abstract fun getAnnotationsElements(): List<T>
    abstract fun process():R
}