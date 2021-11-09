package com.kuzmin.Repository

import com.google.gson.Gson
import com.kuzmin.Model.PostModel
import com.kuzmin.PostData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

import java.io.File

class PostRepositoryInMemoryConcurrentImpl : PostRepository {
    private var nextId = 1L
    private val items = PostData.getDataBase()
    private val mutex = Mutex()
    override suspend fun getAll(): List<PostModel> =
        mutex.withLock {
            for (post in items) {
                val index = items.indexOf(post)
                val copy = post.copy(viewPost = post.viewPost + 1)
                items[index] = copy
            }
            items.reversed()
        }

    override suspend fun getById(id: Long): PostModel? {
        return items.find { it.id == id }
    }

    override suspend fun save(item: PostModel): PostModel {
        return when (val index = items.indexOfFirst { it.id == item.id }) {
            -1 -> {
                val copy = item.copy(id = nextId++)
                items.add(copy)
                File("pst.json").writeText(Gson().toJson(items))
                copy
            }
            else -> {
                items[index] = item
                File("pst.json").writeText(Gson().toJson(items))
                item
            }
        }
    }

    override suspend fun removeById(id: Long) {
        items.removeIf { it.id == id }
    }

    override suspend fun likeById(id: Long): PostModel? {
        return when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likeTxt = item.likeTxt + 1)
                items[index] = copy
                File("pst.json").writeText(Gson().toJson(items))
                copy
            }
        }
    }

    override suspend fun dislikeById(id: Long): PostModel? {
        return when (val index = items.indexOfFirst { it.id == id }) {
            -1 -> null
            else -> {
                val item = items[index]
                val copy = item.copy(likeTxt = item.likeTxt - 1)
                items[index] = copy
                File("pst.json").writeText(Gson().toJson(items))
                copy
            }
        }
    }

    override suspend fun new(txt: String?, author: String?): List<PostModel> =
        mutex.withLock {
            val new = PostModel(id = items.size.toLong(),txt = txt,author = author)
            items.add(new)
            File("pst.json").writeText(Gson().toJson(items))
            items

        }
}
