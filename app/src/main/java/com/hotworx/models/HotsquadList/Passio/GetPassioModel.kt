package com.hotworx.models.HotsquadList.Passio

import com.google.gson.annotations.SerializedName

data class GetPassioModel(
    @SerializedName("selectedUnit"     ) var selectedUnit     : String?                 = null,
    @SerializedName("iconId"           ) var iconId           : String?                 = null,
    @SerializedName("barcode"          ) var barcode          : String?                 = null,
    @SerializedName("name"             ) var name             : String?                 = null,
    @SerializedName("servingUnits"     ) var servingUnits     : ArrayList<ServingUnits> = arrayListOf(),
    @SerializedName("mealLabel"        ) var mealLabel        : String?                 = null,
    @SerializedName("id"               ) var id               : String?                 = null,
    @SerializedName("servingSizes"     ) var servingSizes     : ArrayList<ServingSizes> = arrayListOf(),
    @SerializedName("ingredients"      ) var ingredients      : ArrayList<Ingredients>  = arrayListOf(),
    @SerializedName("passioID"         ) var passioID         : String?                 = null,
    @SerializedName("uuid"             ) var uuid             : String?                 = null,
    @SerializedName("selectedQuantity" ) var selectedQuantity : Int?                    = null,
    @SerializedName("createdAt"        ) var createdAt        : Double?                 = null,
    @SerializedName("openFoodLicense"  ) var openFoodLicense  : String?                 = null,
    @SerializedName("scannedUnitName"  ) var scannedUnitName  : String?                 = null,
    @SerializedName("details"          ) var details          : String?                 = null,
    @SerializedName("entityType"       ) var entityType       : String?                 = null
)

data class ServingUnits(
    @SerializedName("unitName") var unitName: String? = null,
    @SerializedName("unitQuantity") var unitQuantity: Double? = null
)

data class ServingSizes(
    @SerializedName("sizeName") var sizeName: String? = null,
    @SerializedName("sizeValue") var sizeValue: Double? = null
)

data class Ingredients(
    @SerializedName("ingredientName") var ingredientName: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("unit") var unit: String? = null
)