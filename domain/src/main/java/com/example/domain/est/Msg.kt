package com.example.domain.est

import com.example.libannotation.Mapping
import com.example.libannotation.MappingField

@Mapping(targetClass = MsgNEw::class)
class Msg {
    @MappingField(targetFieldName = "contentNew")
    var content:String = ""
    var fs:Int =1013
}