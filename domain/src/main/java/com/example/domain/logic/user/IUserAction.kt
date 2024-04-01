package com.example.domain.logic.user

interface IUserAction {
    fun saveUser()

    fun loadUser(uid:Int)
}