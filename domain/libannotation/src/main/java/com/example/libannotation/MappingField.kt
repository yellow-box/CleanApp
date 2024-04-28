package com.example.libannotation

@Target (AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class MappingField(val targetFieldName:String = ""){

}
