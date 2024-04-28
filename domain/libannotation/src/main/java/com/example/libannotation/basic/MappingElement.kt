package com.example.libannotation.basic

import com.example.libannotation.Mapping
import com.google.devtools.ksp.symbol.KSClassDeclaration

class MappingElement(val declaration: KSClassDeclaration) : MElement {
    override val annotationName: String
        get() = Mapping::class.java.name
}