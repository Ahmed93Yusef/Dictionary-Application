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
        var inputText: String = ""
        var inputLanguage: String = ""
        var outputLanguage: String = ""
        var detectText = ""
        var textTranslate = ""
    }
}