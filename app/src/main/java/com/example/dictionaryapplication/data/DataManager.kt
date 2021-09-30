package com.example.dictionaryapplication.data

import com.example.dictionaryapplication.data.response.languages.LanguagesData

object DataManager {
    private val languagesDataList = mutableListOf<LanguagesData>()

    val languageOutputList: List<LanguagesData>
        get() = languagesDataList

    fun addLanguagesData(list: List<LanguagesData>) {
        languagesDataList.addAll(list)
    }

    object Setting {
        var inputText: String = "hello"
        var inputLanguage: String = "en"
        var outputLanguage: String = "es"
        var detectText = ""
        var textTranslate = ""
    }
}