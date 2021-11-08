package com.kuzmin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.Model.PostModel
import com.kuzmin.Model.UserModel
import java.io.File

object UserData {
    fun getDataBase() : ArrayList<UserModel>{
        return try{
            val fileName = "user.json"
            val type = object  : TypeToken<List<UserModel>>(){}.type
            val result : ArrayList<UserModel> = Gson().fromJson(File(fileName).readText(), type)
            result
        } catch (e: Exception){
            ArrayList<UserModel>()
        }
    }
}