package com.example.libannotation.basic

import com.example.libannotation.MappingConverter

class MappingConvertElement(
    val srcClassInfo: ClassInfo,
    val targetClassInfo: ClassInfo,
    val env: MEnv
) : MElement {
    override val annotationName: String = MappingConverter::class.java.name
}