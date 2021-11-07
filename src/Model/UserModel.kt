package com.kuzmin.Model

import io.ktor.auth.*


data class UserModel (
    val id : Long,
    val username : String,
    val password : String
): Principal {
}