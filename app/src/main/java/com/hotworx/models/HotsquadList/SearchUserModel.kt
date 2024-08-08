package com.hotworx.models.HotsquadList

import com.google.gson.annotations.SerializedName

class SearchUserModel (
    @SerializedName("userList")
    var userList: List<UserModel>
)
