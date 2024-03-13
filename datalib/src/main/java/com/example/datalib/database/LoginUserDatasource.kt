package com.example.datalib.database

import com.example.domain.device.ILoginUser

class LoginUserDatasource:ILoginUser {
    override fun getUid(): Int {
        return 3
    }

    override fun getName(): String {
       return "小孔"
    }
}