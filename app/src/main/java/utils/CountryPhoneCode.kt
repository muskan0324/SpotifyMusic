package com.locon.core.data

import com.google.gson.annotations.SerializedName

data class CountryPhoneCode(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url_name")
    val urlName: String = "",
    @SerializedName("country_code")
    val phoneCode: String = ""
)

data class CountryCategory(val category: String, val countryPhoneCodes: List<CountryPhoneCode>)
