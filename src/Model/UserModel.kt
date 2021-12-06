package com.kuzmin.Model

import io.ktor.auth.*


data class UserModel (
    val id : Long = 0,
    val username : String,
    val password : String,
    val token: String,
    val tokenDevice: String = ""
): Principal
