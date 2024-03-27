package com.example.datalib.database

import com.example.domain.device.ILoginUser

class LoginUserDatasource : ILoginUser {
    private var uid: Int = 5
    private var name: String = "小吴"
    override fun getUid(): Int {
        return uid
    }

    override fun getName(): String {
        return name
    }

    override fun saveName(name: String) {
        this.name = name;
    }

    override fun saveUid(uid: Int) {
        this.uid = uid
    }
}