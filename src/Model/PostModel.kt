package com.kuzmin.Model

data class PostModel(
    val id: Long = 0,
    val author: String? = null,
    val data: Long = 0,
    val txt: String? = null,
    var like: Boolean = false,
    val comment: Boolean = false,
    val share: Boolean = false,
    var likeTxt: Int = 0,
    val commentTxt: Int = 0,
    val shareTxt: Int = 0,
    val adress: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val type: PostType = PostType.Reposts,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewPost: Long = 0


)
enum class PostType {
    Reposts,
    YoutubeVideo,
    SponsoredPosts
}