package com.example.libannotation.basic

import com.example.libannotation.ClassMapping
import com.example.libannotation.Ex
import com.example.libannotation.MappingConverter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class MappingConverterStep(env: MEnv, val classMappingMap: Map<String, ClassMappingRecord>) :
    MProcessStep<MappingConvertElement, Unit>(env) {
    companion object {
        const val NAME_SRC_TO_TARGET = "convert"
        const val NAME_TARGET_TO_SRC = "revers"
    }

    override val annotationClassName: String
        get() = MappingConverter::class.java.name

    override fun getAnnotationsElements(): List<MappingConvertElement> {
        return env.resolver.getSymbolsWithAnnotation(annotationClassName)
            .filterIsInstance<KSClassDeclaration>().map {
                val srcToTarget = getSrcNameClassAndTargetClass(it)
                MappingConvertElement(srcToTarget.first, srcToTarget.second, env)
            }.toList()
    }

    /**
     * @return srcClass qualifiedName to targetClass qualifiedName
     */
    private fun getSrcNameClassAndTargetClass(classDeclaration: KSClassDeclaration): Pair<ClassInfo, ClassInfo> {
        return if (isValidType(classDeclaration.classKind)) {
            env.logger.warn("getSrcNameClassAndTargetClass top，name=${classDeclaration.simpleName.getShortName()}")
            var srcClassInfo = ClassInfo.NoneInfo
            var targetClassInfo = ClassInfo.NoneInfo
            var curTypeArgumentsIndex = 0
            //获取类中声明的泛型参数列表
            classDeclaration.superTypes.map { kspTypeRef ->
                if ((kspTypeRef.resolve().declaration.qualifiedName?.asString()
                        ?: "") == ClassMapping::class.java.name
                ) {
                    kspTypeRef.element?.typeArguments?.let {
                        for (typeArg in it) {
                            if (curTypeArgumentsIndex == 0) {
                                srcClassInfo = ClassInfo(
                                    typeArg.type?.resolve()?.toClassName()?.packageName ?: "",
                                    typeArg.type?.resolve()?.toClassName()?.simpleName ?: ""
                                )
                            } else if (curTypeArgumentsIndex == 1) {
                                targetClassInfo = ClassInfo(
                                    typeArg.type?.resolve()?.toClassName()?.packageName ?: "",
                                    typeArg.type?.resolve()?.toClassName()?.simpleName ?: ""
                                )
                            } else {
                                break
                            }
                            curTypeArgumentsIndex++
                        }
                    }
                }
            }.count()
            srcClassInfo to targetClassInfo
        } else {
            env.logger.error("MappingConverter should be add to the \"ClassMapping\" direct subClass ")
            ClassInfo.NoneInfo to ClassInfo.NoneInfo
        }
    }

    private fun isValidType(kind: ClassKind): Boolean {
        return kind == ClassKind.CLASS || kind == ClassKind.INTERFACE
    }

    override fun process() {
        env.resolver.getSymbolsWithAnnotation(annotationClassName)
            .filterIsInstance<KSClassDeclaration>().map {
                env.logger.warn(
                    "MappingConverterStep KSClassDeclaration with map,${it.simpleName.getShortName()}}"
                )
                if (!it.hasSupperClass(ClassMapping::class.java.name)) {
                    return@map
                }
                val superInterfaceInfo =
                    ClassInfo(it.packageName.asString(), it.simpleName.getShortName())
                val mappingClassInfo = getSrcNameClassAndTargetClass(it)
                if (mappingClassInfo.first != ClassInfo.NoneInfo && mappingClassInfo.second != ClassInfo.NoneInfo) {
                    val classMappingRecord =
                        if (classMappingMap[mappingClassInfo.first.getQualifiedName()] != null) {
                            classMappingMap[mappingClassInfo.first.getQualifiedName()]
                        } else {
                            classMappingMap[mappingClassInfo.second.getQualifiedName()]
                        }
                    classMappingRecord?.apply {
                        writeFile(superInterfaceInfo, mappingClassInfo, this)
                    }
                }

            }.count()
    }

    private fun KSClassDeclaration.hasSupperClass(
        supperClassQualifiedName: String
    ): Boolean {
        return this.superTypes.any {
            it.resolve().declaration.qualifiedName?.asString()
                .toString() == supperClassQualifiedName
        }
    }

    private fun writeFile(
        superInterfaceInfo: ClassInfo,
        classMapInfo: Pair<ClassInfo, ClassInfo>,
        classMappingRecord: ClassMappingRecord
    ) {
        val implClassname =
            Ex.getGenClassName(superInterfaceInfo.getQualifiedName())
        val fileSpecBuilder = FileSpec.builder(
            classMappingRecord.srcClassInfo.packageName,
            implClassname
        ).addImport(classMapInfo.first.packageName, classMapInfo.first.classSimpleName)
            .addImport(classMapInfo.second.packageName, classMapInfo.second.classSimpleName)
            .addImport(superInterfaceInfo.packageName, superInterfaceInfo.classSimpleName)

        val srcClassInfo = classMappingRecord.srcClassInfo
        val targetClassInfo =
            if (classMapInfo.first == srcClassInfo) classMapInfo.second else classMapInfo.first

        fun buildFuncSpec(
            funcName: String,
            srcClassInfo: ClassInfo,
            targetClassInfo: ClassInfo,
            isReverse: Boolean
        ): FunSpec {
            val funcBuilder =
                FunSpec.builder(funcName)
                    .addParameter(
                        ParameterSpec.builder(
                            "src", ClassName(srcClassInfo.packageName, srcClassInfo.classSimpleName)
                        ).build()
                    )
                    .addStatement("val target =${targetClassInfo.classSimpleName}()")
                    .addModifiers(KModifier.OVERRIDE)
            classMappingRecord.filedMaps.forEach {
                if (isReverse) {
                    funcBuilder.addStatement("target.${it.srcFieldName} = src.${it.targetFieldName}")

                } else {
                    funcBuilder.addStatement("target.${it.targetFieldName} = src.${it.srcFieldName}")
                }
            }
            return funcBuilder.returns(
                ClassName(
                    targetClassInfo.packageName,
                    targetClassInfo.classSimpleName
                )
            ).addStatement("return target").build()
        }

        fileSpecBuilder.addType(
            TypeSpec.classBuilder(implClassname)
                .superclass(
                    ClassName(
                        superInterfaceInfo.packageName,
                        superInterfaceInfo.classSimpleName
                    )
                    //target To Src
                ).addFunction(
                    buildFuncSpec(
                        NAME_SRC_TO_TARGET,
                        srcClassInfo,
                        targetClassInfo,
                        false
                    )
                )
                .addFunction(
                    buildFuncSpec(
                        NAME_TARGET_TO_SRC,
                        targetClassInfo,
                        srcClassInfo,
                        true
                    )
                )
                .build()
        ).build().writeTo(
            env.codeGenerator,
            //这里对 依赖的源文件的处理不够完善，需要加上convertor 用到的 原类和target类
            Dependencies(true, classMappingRecord.classDeclaration.containingFile!!)
        )
        env.logger.info("MappingConverterStep finish write，fileName=${classMappingRecord.classDeclaration.containingFile?.fileName}")
    }
}