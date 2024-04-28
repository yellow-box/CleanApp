package com.example.libannotation

import com.example.libannotation.basic.MElement

class MFieldElement : MElement {
    override val annotationName: String
        get() = MappingField::class.java.name
}