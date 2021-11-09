package com.kuzmin.Repository

import com.google.gson.Gson
import com.kuzmin.Model.UserModel
import com.kuzmin.UserData
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

class UserRepositoryInMemoryWithMutexImpl : UserRepository {
    private var nextId = 1L
    private val items = UserData.getDataBase()
    private val mutex = Mutex()
    override suspend fun getAll(): List<UserModel> {
        mutex.withLock {
            return items.toList()
        }
    }

    override suspend fun getById(id: Long): UserModel? {
        mutex.withLock {
            return items.find { it.id == id }
        }
    }

    override suspend fun getByIds(ids: Collection<Long>): List<UserModel> {
        mutex.withLock {
            return items.filter { ids.contains(it.id) }
        }
    }

    override suspend fun getByUsername(username: String): UserModel? {
        mutex.withLock {
            return items.find { it.username == username }
        }
    }

    override suspend fun save(item: UserModel): UserModel {
        mutex.withLock {
            return when (val index = items.indexOfFirst { it.id == item.id }) {
                -1 -> {
                    val copy = item.copy(id = items.size.toLong())
                    items.add(copy)
                    File("user.json").writeText(Gson().toJson(items))
                    copy
                }
                else -> {
                    val copy = items[index].copy(username = item.username, password = item.password)
                    items[index] = copy
                    File("user.json").writeText(Gson().toJson(items))
                    copy
                }
            }
        }
    }

    override suspend fun addUser(item: UserModel): Boolean {
        mutex.withLock {
            return when (val index = items.indexOfFirst { it.username == item.username }) {
                -1 -> {
                    val copy = item.copy(id = items.size.toLong())
                    items.add(copy)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override suspend fun getSizeListUser(): Long {
        return items.size.toLong()
    }
}