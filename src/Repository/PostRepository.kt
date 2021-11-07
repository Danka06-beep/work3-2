package com.kuzmin.Repository

import com.kuzmin.Model.PostModel

interface PostRepository {
    suspend fun getAll(): List<PostModel>
    suspend fun getById(id: Long): PostModel?
    suspend fun save(item: PostModel): PostModel
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long): PostModel?
    suspend fun dislikeById(id: Long): PostModel?
    suspend fun new(txt: String?, author: String?): List<PostModel>
}