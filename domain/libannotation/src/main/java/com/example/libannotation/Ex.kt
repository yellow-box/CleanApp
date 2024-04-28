package com.example.libannotation

object Ex {
    //因为一个 MappingConverter 类对应一个实现类，故其生成类可用  MappingConverter的 包名 拼 类名 拼 ”impl“
    fun getGenClassName(qualifiedName:String) =
        listOf(qualifiedName.replace(".", "_"),"impl").joinToString("_")

}