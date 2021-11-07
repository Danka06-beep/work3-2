package com.kuzmin.service

import com.kuzmin.Model.PostModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.dto.PostRequestDto
import com.kuzmin.dto.PostResponseDto
import io.ktor.features.*

class PostService(val repo: PostRepository) {
    suspend fun getAll(): List<PostResponseDto>{
        return repo.getAll().map{PostResponseDto.fromModel(it) }
    }
    suspend fun getById(id: Long): PostResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return PostResponseDto.fromModel(model)
    }

    suspend fun save(input: PostRequestDto): PostResponseDto {
        val model = PostModel(id = input.id, author = input.author, txt = input.txt)
        return PostResponseDto.fromModel(repo.save(model))
    }
}