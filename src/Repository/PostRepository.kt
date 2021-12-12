package com.kuzmin.Repository

import com.kuzmin.Model.AttachmentModel
import com.kuzmin.Model.PostModel
import com.kuzmin.Model.RepostModel

interface PostRepository {
    suspend fun getAll(): List<PostModel>
    suspend fun getById(id: Long): PostModel?
    suspend fun save(item: PostModel): PostModel
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long, userId:Long?): PostModel?
    suspend fun dislikeById(id: Long,userId:Long?): PostModel?
    suspend fun new(txt: String?, author: String?): List<PostModel>
    suspend fun repost(item: RepostModel): RepostModel?
    suspend fun getfive():List<PostModel>
    suspend fun getOld(id: Long): List<PostModel>
    suspend fun newPost(txt: String?, attachment: AttachmentModel?, autorName: String?): List<PostModel>

}

