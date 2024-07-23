package com.hotworx.requestEntity

 data class ExtendedBaseModel(
    val msg: String, // OTP Generated
    val token: String, // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDEwNzc5MzMsImV4cCI6MTcwMzY2OTkzMywiZGF0YSI6eyJ1c2VyX2lkIjoiNzRhZTFiYmJjY2NmOWViYmM2Zjk2N2VkN2YyMThiZjQxNzAxMDc3OTMyMTcwMTA3ODkwOSJ9fQ.Ox3JCcODcWcx2c9nMaVdPkJfXNf-VOBrA0bE5LAEtZ4
    val two_factor: String, // yes
    val error: String?
)