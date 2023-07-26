package com.locon.core.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import timber.log.Timber
import java.io.IOException

object PhoneCodesUtil {

    private var phoneCodes: List<CountryPhoneCode> = listOf()

    val defaultPhoneCode = CountryPhoneCode(
        id = 1,
        name = "India",
        urlName = "in",
        phoneCode = "+91"
    )

    fun fetchPhoneCodes(context: Context) {
        var jsonString = ""
        try {
            jsonString = context.assets.open("phone_codes.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
//            Timber.d(ioException)
        }

        val listCountryType = object : TypeToken<List<CountryPhoneCode>>() {}.type
        phoneCodes = Gson().fromJson(jsonString, listCountryType)
    }

    fun getAllCountryPhoneCodes(): List<CountryPhoneCode> {
        return phoneCodes.subList(5, phoneCodes.size)
    }

    fun getTopCountryPhoneCodes(): List<CountryPhoneCode> {
        return phoneCodes.subList(0, 5)
    }
    fun getAllCountry():List<CountryPhoneCode>{
        return phoneCodes
    }
}
