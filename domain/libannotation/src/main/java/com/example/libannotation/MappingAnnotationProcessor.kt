package com.example.libannotation

import com.example.libannotation.basic.ClassMappingRecord
import com.example.libannotation.basic.MEnv
import com.example.libannotation.basic.MappingConverterStep
import com.example.libannotation.basic.MappingStep
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitor
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.OutputStream
import kotlin.reflect.KClass

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

fun Sequence<KSAnnotation>.getAnnotation(target: String): KSAnnotation {
    return getAnnotationIfExist(target)
        ?: throw NoSuchElementException("Sequence contains no element matching the predicate.")
}

fun Sequence<KSAnnotation>.getAnnotationIfExist(target: String): KSAnnotation? {
    for (element in this) if (element.shortName.asString() == target) return element
    return null
}

fun Sequence<KSAnnotation>.hasAnnotation(target: String): Boolean {
    for (element in this) if (element.annotationType.resolve().declaration.qualifiedName?.asString() == target) return true
    return false
}

fun <T> List<KSValueArgument>.getParameterValue(target: String): T {
    return getParameterValueIfExist(target)
        ?: throw NoSuchElementException("Sequence contains no element matching the predicate.")
}


fun <T> List<KSValueArgument>.getParameterValueIfExist(target: String): T? {
    for (element in this) if (element.name!!.asString() == target) (element.value as? T)?.let { return it }
    return null
}

class MappingAnnotationProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) :
    SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("MappingAnnotationProcessor")
        val mappingSymbol = resolver.getSymbolsWithAnnotation(Mapping::class.java.name)
            .filterIsInstance<KSClassDeclaration>()
        val packageSymbols = resolver.getPackagesWithAnnotation(Mapping::class.java.name)
        val ret = mappingSymbol.filter { !it.validate() }.toList()
        mappingSymbol.filter { it.validate() }.forEach {
            it.accept(MappingVisitor(), Unit)
        }
        var logStr = ""
        packageSymbols.forEach {
            logStr += it + ","
        }
        logger.warn("log packageSymbols:${logStr}")
        //返回被拒绝处理的注解
        return ret
    }

    //遍历 Kotlin 编译器的 AST（抽象语法树），
    inner class MappingVisitor() : KSVisitorVoid() {
        private var file: OutputStream? = null

        override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
            file?.appendText("visitAnnotation , ")
        }

        //会在访问 Kotlin 源代码中的类声明时被调用
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

            logger.info("[testlog],${classDeclaration.packageName}")
            var targetClass: KClass<*>? = null

            val packageName = classDeclaration.packageName.asString()
            val className = "${classDeclaration.simpleName.asString()},"
            file = codeGenerator.createNewFile(
                Dependencies(true, classDeclaration.containingFile!!),
                packageName.toString(), className
            )
//            val fileSpec = FileSpec.builder(packageName,className).build().writeTo(codeGenerator,  Dependencies(true, classDeclaration.containingFile!!),)
            var targetClassStr = ""
            classDeclaration.annotations.forEach { annonation ->
                annonation.arguments.forEach { arg ->
                    if (arg.name!!.asString() == "targetClass") {
                        targetClassStr = arg.value.toString()
//                        targetClass = arg.value as KClass<*>
                    }
                }
            }
            file?.appendText("class MsgMapping{\n")
            classDeclaration.getDeclaredProperties().forEach {
                file?.appendText("property has:${it.simpleName}\n")
            }
            classDeclaration.annotations.forEach { annonation ->
                annonation.arguments.forEach { arg ->
                    if (arg.name!!.asString() == "targetClass") {
                        file?.appendText("val taretObj =${(arg.value.toString())}() \n")
                        targetClass = arg.value as? KClass<*>
                    }
                }
            }

            classDeclaration.containingFile!!.annotations.forEach {
                file?.appendText("annotation has:${it.shortName.asString()}\n")
            }
            file?.appendText("3:${classDeclaration.containingFile!!.fileName}\n")
            file?.appendText("4:${targetClass?.simpleName}\n")
            file?.appendText("5 size=:${classDeclaration.getDeclaredProperties().count()}\n")
//            classDeclaration.getDeclaredProperties().forEach {
//                visitPropertyDeclarationToCreateConstructor(it)
//            }
            file?.appendText("\n}")

            classDeclaration.primaryConstructor!!.accept(this, data)
            classDeclaration.getDeclaredProperties().forEach {
                it.accept(this, Unit)
            }
            file?.close()
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
//            super.visitPropertyDeclaration(property, data)
            property.annotations.forEach { ann ->
                ann.accept(object : KSEmptyVisitor<String, Unit>() {
                    override fun visitAnnotation(annotation: KSAnnotation, data: String) {
                        file?.appendText("visit annoation in property,${annotation.shortName.asString()}\n")
                        annotation.arguments.forEach {
                            it.accept(this, data)
                        }
                    }

                    override fun visitValueArgument(valueArgument: KSValueArgument, data: String) {
                        file?.appendText("visit visitValueArgument in property,name=${valueArgument.name!!.asString()},value=${valueArgument.value},data=${data}\n")
                    }

                    override fun defaultHandler(node: KSNode, data: String) {
                        file?.appendText("defaultJHandler\n")
                    }

                }, property.simpleName.asString())
            }
            file?.appendText("visitPropertyDeclaration\n")
        }

        private fun visitPropertyDeclarationToCreateConstructor(property: KSPropertyDeclaration) {
            val typeResolve = property.type.resolve()
            //      val annotationValue = annotation.arguments.find { it.name?.asString() == "yourPropertyName" }?.value
            file?.appendText("visit proeore\n")
            property.annotations.forEach {
                file?.appendText("ann:${it.annotationType.resolve().declaration.qualifiedName?.asString()}\n")
                it.arguments.forEach {
                    file?.appendText("arguments:${it.name!!.asString()},value=${it.value} \n")
                }
            }
            if (property.annotations.hasAnnotation(MappingField::class.java.name).not()) {
                val type = typeResolve.declaration.qualifiedName?.asString() ?: ""
                file?.appendText("6 peoerty not :${property.simpleName.asString()},type:${type}\n")
            } else {
                val type = typeResolve.declaration.qualifiedName?.asString() ?: ""
                val targetFieldName =
                    typeResolve.annotations.getAnnotationIfExist(MappingField::class.java.name)
                        ?.arguments?.getParameterValue<String>("targetFieldName")
                file?.appendText("6 peoerty has:${property.simpleName.asString()},type:${type},t_Field_name=${targetFieldName}\n")
            }
        }

    }
}

class MainMappingAnnotationProcess(val codeGenerator: CodeGenerator, val logger: KSPLogger) :
    SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("MainMappingAnnotationProcess")
        val env = MEnv(resolver, codeGenerator, logger)
//        val step = MappingConverterStep(env)
        val step1 = MappingStep(env)
        val classMappingMap = step1.process()
//        classMappingMap.print(logger)
        val step2 = MappingConverterStep(env, classMappingMap)
        step2.process()
        //返回未处理的 注解
        return emptyList()
    }

}

fun Map<String, ClassMappingRecord>.print(logger: KSPLogger) {
    val stringBuilder = StringBuilder()
    for (en in this.entries) {
        val cm =en.value
        stringBuilder.append("key:${en.key}，class:${cm.srcClassInfo.classSimpleName}: ")
        cm.filedMaps.map {
            stringBuilder.append("[src:${it.srcFieldName},target:${it.targetFieldName}]; ")
        }
        stringBuilder.append("\n")
    }
    logger.warn("MainMappingAnnotationProcess , result=${stringBuilder}")
}


class MappingAnnotationProcessProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MainMappingAnnotationProcess(environment.codeGenerator, environment.logger)
//        return MappingAnnotationProcessor(environment.codeGenerator, environment.logger)
    }
}