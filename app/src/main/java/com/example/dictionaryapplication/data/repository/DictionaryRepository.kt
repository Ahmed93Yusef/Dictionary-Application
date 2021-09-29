package com.example.dictionaryapplication.data.repository

import com.example.dictionaryapplication.data.response.languages.LanguagesData

object DictionaryRepository {
    private val languagesDataList = mutableListOf<LanguagesData>()
    private val translateData = mutableListOf<String>()

    val languageOutputList: List<LanguagesData>
        get() = languagesDataList

    val translateText: List<String>
        get() = translateData

    fun addLanguagesData(list: List<LanguagesData>){
        languagesDataList.addAll(list)
    }

    fun addTranslateData(list: String?){
        translateData.clear()
        list?.let { translateData.add(it) }

    }

    object Setting {
        var inputText: String = "book"
        var inputLanguage: String = "en"
        var outputLanguage: String = "es"

    }
}