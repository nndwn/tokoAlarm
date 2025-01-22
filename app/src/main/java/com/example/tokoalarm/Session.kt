package com.example.tokoalarm

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class Session(private val preferencesManager: PreferencesManager) {
    fun getIdAlat(): String? = runBlocking {
        preferencesManager.idAlatFlow.first()
    }
    fun setIdAlat(idAlat: String) = runBlocking {
        preferencesManager.setIdAlat(idAlat)
    }
    fun getPwd() : String? = runBlocking {
        preferencesManager.pwdFlow.first()
    }
    fun setPwd(pwd: String) = runBlocking {
        preferencesManager.setPwd(pwd)
    }
    fun getIdUser() :String? = runBlocking {
        preferencesManager.idUserFlow.first()
    }
    fun setIdUser(idUser : String) = runBlocking {
        preferencesManager.setIdUser(idUser)
    }
    fun getNameUser() :String? = runBlocking {
        preferencesManager.nameUserFlow.first()
    }
    fun setNameUser(nameUser : String) = runBlocking {
        preferencesManager.setNameUser(nameUser)
    }
    fun getPhone() :String? = runBlocking {
        preferencesManager.phoneFlow.first()
    }
    fun setPhone(phone : String) = runBlocking {
        preferencesManager.setPhone(phone)
    }

    fun logout () = runBlocking {
        preferencesManager.setIdUser("")
        preferencesManager.setNameUser("")
        preferencesManager.setPhone("")
        preferencesManager.setPwd("")
    }
}