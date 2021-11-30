package com.kuzmin.dto

import com.kuzmin.Model.AttachmentModel
import com.kuzmin.Model.PostModel
import com.kuzmin.Model.PostType

data class PostResponseDto(
    val id: Long = 0,
    val author: String? = null,
    val data: Long = 0,
    val txt: String? = null,
    var like: Boolean = false,
    val comment: Boolean = false,
    val share: Boolean = false,
    var likeTxt : Int = 0,
    val commentTxt : Int = 0,
    val shareTxt : Int  = 0,
    val adress : String? = null,
    val coordinates : Pair<Double,Double>? = null,
    val type: PostType = PostType.Reposts,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewPost: Long = 0,
    val repost:PostModel?=null,
    val attachment: AttachmentModel? = null
) {
    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            data = model.data,
            txt = model.txt,
            like = model.like,
            comment = model.comment,
            share = model.share,
            likeTxt = model.likeTxt,
            commentTxt = model.commentTxt,
            shareTxt = model.shareTxt,
            adress = model.adress,
            coordinates = model.coordinates,
            type = model.type,
            url = model.url,
            dateRepost = model.dateRepost,
            autorRepost = model.autorRepost,
            hidePost = model.hidePost,
            viewPost = model.viewPost,
            repost = model.repost,
            attachment = model.attachment
        )
    }
}