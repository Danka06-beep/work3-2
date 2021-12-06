package com.kuzmin.dto

import com.kuzmin.Model.UserModel

class UserResponeDto(username : String) {
    val username = username

    companion object {
        fun fromModel(model: UserModel) = UserResponeDto(
            username = model.username
        )
    }

    override fun toString(): String {
        return "username : $username"
    }
}