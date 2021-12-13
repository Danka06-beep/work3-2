package com.kuzmin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.Model.PostModel
import com.kuzmin.Model.RepostModel
import java.io.File

object RepostData {
    fun getDataBaseRepost() : ArrayList<RepostModel>{
        return try{
            val fileName = "repsot.json"
            val type = object  : TypeToken<List<RepostModel>>(){}.type
            val result : ArrayList<RepostModel> = Gson().fromJson(File(fileName).readText(), type)
            result
        } catch (e: Exception){
            ArrayList<RepostModel>()
        }
    }
}