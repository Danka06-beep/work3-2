package com.kuzmin.dto


import com.kuzmin.Model.RepostModel

data class RepostResponseDto( val id: Long = 0,
                              val authorRepost: String? = null,
                              val txtRepost: String? = null,) {
    companion object {
        fun fromRepostModel(model: RepostModel) = RepostResponseDto(
            id = model.id,
            authorRepost = model.authorRepost,
            txtRepost = model.txtRepost

        )
    }
}
