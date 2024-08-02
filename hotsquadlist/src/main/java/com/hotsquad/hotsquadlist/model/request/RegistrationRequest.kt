package com.hotsquad.hotsquadlist.model.request

import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.StringUtils

data class RegistrationRequest(

    @SerializedName("name")
    var name: String = StringUtils.EMPTY

)