package com.example.libannotation.basic

class ClassInfo(val packageName: String, val classSimpleName: String) {
    companion object {
        val NoneInfo = ClassInfo("", "")
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false
        return if (other is ClassInfo) {
            packageName == other.packageName && classSimpleName == other.classSimpleName
        } else {
            false
        }
    }

    fun getQualifiedName() = "$packageName.$classSimpleName"
}
