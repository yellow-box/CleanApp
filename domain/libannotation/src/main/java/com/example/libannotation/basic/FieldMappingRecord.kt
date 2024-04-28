package com.example.libannotation.basic

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class FieldMappingRecord(val srcFieldName: String, val targetFieldName: String) {
    companion object {
        val NONE = FieldMappingRecord("", "")
    }
}

class ClassMappingRecord(
    val srcClassInfo: ClassInfo,
    val classDeclaration: KSClassDeclaration,
    val filedMaps: List<FieldMappingRecord>
)