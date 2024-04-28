package com.example.libannotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class  Mapping(val targetClass: KClass<*>)  {
}