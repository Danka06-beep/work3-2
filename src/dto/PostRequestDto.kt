package com.kuzmin.dto

data class PostRequestDto(
    val id: Long,
    val author: String,
    val txt: String? = null,
    var like: Boolean = false,
    var liketxt: Int = 0,
    val dateRepost: Long? = null,
    val authorReposts: String? = null,
    val authorId: Long
) {
}