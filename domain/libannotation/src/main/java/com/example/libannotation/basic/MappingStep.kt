package com.example.libannotation.basic

import com.example.libannotation.Mapping
import com.example.libannotation.MappingField
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import java.util.ArrayList
import java.util.HashMap

class MappingStep(env: MEnv) : MProcessStep<MappingElement, Map<String, ClassMappingRecord>>(env) {
    override val annotationClassName: String
        get() = Mapping::class.java.name

    override fun getAnnotationsElements(): List<MappingElement> {
        return env.resolver.getSymbolsWithAnnotation(annotationClassName)
            .filterIsInstance<KSClassDeclaration>().map {
                MappingElement(it)
            }.toList()
    }

    override fun process(): Map<String, ClassMappingRecord> {
        val mappingElements = getAnnotationsElements()
        val classMappingMap = HashMap<String, ClassMappingRecord>()
        mappingElements.forEach {
            if (it.declaration.classKind != ClassKind.CLASS) {
                env.logger.error("Mapping annotation should add to specific class")
            } else {
                val processor = MappingFieldProcessor(env.logger)
                val fieldMappingList = it.declaration.getAllProperties().mapNotNull { property ->
                    processor.process(property)
                }.toList()
                val srcClassInfo = ClassInfo(
                    it.declaration.packageName.asString(),
                    it.declaration.simpleName.asString()
                )
                classMappingMap[it.declaration.qualifiedName?.asString().toString()] =
                    (ClassMappingRecord(srcClassInfo, it.declaration, fieldMappingList))
            }
        }
        return classMappingMap
    }
}


class MappingFieldProcessor(val logger: KSPLogger) {
    companion object {
        const val NAME_TARGET_FIELD = "targetFieldName"
    }

    fun process(declaration: KSPropertyDeclaration): FieldMappingRecord? {
        //确保getter 和 setter 是public
        if (!declaration.validate() || !checkAccessorModifier(declaration)) {
            return null
        }

        var srcFieldName: String? = null
        var targetFieldName: String? = null
        // 没有MappingField注解 表示不需要进行映射
        declaration.annotations.firstOrNull {
            (it.annotationType.resolve().declaration.qualifiedName?.asString()
                .toString()) == MappingField::class.java.name
        }?.apply {
            //注解上 字段参数名称
//                logger.warn("MappingFieldProcessor annotation name =${it.arguments[0].name?.asString() ?: "no arge"}")
//                //注解上的字段 实际传入的值
//                logger.warn("MappingFieldProcessor annotation value =${it.arguments[0].value ?: "no arge"}")
            val argumentValue = arguments.getArgumentStringValueOrNull(NAME_TARGET_FIELD)
            if (argumentValue.isNullOrEmpty()) {
                srcFieldName = declaration.simpleName.asString()
                targetFieldName = declaration.simpleName.asString()
            } else {
                srcFieldName = declaration.simpleName.asString()
                targetFieldName = argumentValue
            }
        }
        return if (srcFieldName == null) {
            null
        } else {
            FieldMappingRecord(srcFieldName!!, targetFieldName!!)
        }
    }

    private fun checkAccessorModifier(declaration: KSPropertyDeclaration): Boolean {
        return (declaration.getter?.modifiers?.any {
            it == Modifier.PUBLIC
        } ?: false && declaration.setter?.modifiers?.any {
            it == Modifier.PUBLIC
        } ?: false)
    }

}

fun List<KSValueArgument>.getArgumentStringValueOrNull(argumentName: String): String? {
    return this.firstOrNull { arg ->
        (arg.name?.asString().toString()) == argumentName
    }?.value as String
}