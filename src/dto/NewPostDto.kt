package com.kuzmin.dto

import com.kuzmin.Model.PostType

data class NewPostDto(
    val author: String? = null,
    val data: Long = 0,
    val txt: String? = null,
    val adress : String? = null,
    val coordinates : Pair<Double,Double>? = null,
    val type: PostType = PostType.Reposts
) {
}