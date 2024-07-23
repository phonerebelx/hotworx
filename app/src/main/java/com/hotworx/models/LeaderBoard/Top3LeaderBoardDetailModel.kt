package com.hotworx.models.LeaderBoard

data class Top3LeaderBoardDetailModel(
    val `data`: List<Data>,
    val msg: String // success
) {
}