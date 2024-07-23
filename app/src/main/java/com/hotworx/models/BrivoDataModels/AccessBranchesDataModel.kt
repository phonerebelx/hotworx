package com.hotworx.models.BrivoDataModels

data class AccessBranchesDataModel(
    val `data`: List<Data>,
    val data_near_by: List<DataNearBy>,
    val msg: String
)