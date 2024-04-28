package com.example.domain.est

import com.example.libannotation.ClassMapping
import com.example.libannotation.MappingConverter


@MappingConverter
abstract class IMsgMapping: ClassMapping<Msg, MsgNEw>() {

}