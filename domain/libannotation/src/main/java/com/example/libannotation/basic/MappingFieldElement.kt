package com.example.libannotation.basic

import com.example.libannotation.MappingField

class MappingFieldElement<T>(
    private val targetClass: Class<T>,
    private val srcFieldName: String,
    private val targetFieldName: String,
    private val env: MEnv
) :
    MElement {
    override val annotationName: String = MappingField::class.java.name
}