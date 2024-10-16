package com.passio.modulepassio.models.HotsquadList.Passio

import com.passio.modulepassio.Passio.Weight

data class Nutrients(
    val _calcium: Calcium,
    val _calories: Calories,
    val _carbs: Carbs,
    val _cholesterol: Cholesterol,
    val _fat: Fat,
    val _fibers: Fibers,
    val _folicAcid: FolicAcid,
    val _iron: Iron,
    val _magnesium: Magnesium,
    val _monounsaturatedFat: MonounsaturatedFat,
    val _phosphorus: Phosphorus,
    val _polyunsaturatedFat: PolyunsaturatedFat,
    val _potassium: Potassium,
    val _proteins: Proteins,
    val _satFat: SatFat,
    val _selenium: Selenium,
    val _sodium: Sodium,
    val _sugars: Sugars,
    val _transFat: TransFat,
    val _vitaminA: Int,
    val _vitaminB12: VitaminB12,
    val _vitaminB12Added: VitaminB12Added,
    val _vitaminB6: VitaminB12,
    val _vitaminC: VitaminC,
    val _vitaminD: VitaminD,
    val _vitaminE: VitaminE,
    val _vitaminEAdded: VitaminEAdded,
    val _vitaminKDihydrophylloquinone: VitaminKDihydrophylloquinone,
    val _vitaminKMenaquinone4: VitaminKMenaquinone4,
    val _vitaminKPhylloquinone: VitaminKPhylloquinone,
    val _zinc: Zinc,
    val referenceWeight: ReferenceWeight,
    val weight: Weight
)