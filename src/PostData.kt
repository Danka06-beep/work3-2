package com.kuzmin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuzmin.Model.PostModel
import com.kuzmin.Model.RepostModel
import java.io.File

object PostData {
    fun getDataBase() : ArrayList<PostModel>{
        return try{
            val fileName = "pst.json"
            val type = object  : TypeToken<List<PostModel>>(){}.type
            val result : ArrayList<PostModel> = Gson().fromJson(File(fileName).readText(), type)
            result
        } catch (e: Exception){
            ArrayList<PostModel>()
        }
    }
}