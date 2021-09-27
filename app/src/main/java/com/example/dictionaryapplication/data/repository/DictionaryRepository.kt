package com.example.dictionaryapplication.data.repository

import com.example.dictionaryapplication.data.response.detect.DetectData
import com.example.dictionaryapplication.data.response.languages.LanguagesData

object DictionaryRepository {
    private val languagesDataList = mutableListOf<LanguagesData>()
    private val detectDataList = mutableListOf<DetectData>()
    private val translateData = mutableListOf<String>()

    val languageList: List<LanguagesData>
    get() = languagesDataList

    val detectList: List<DetectData>
    get() = detectDataList

    val translateText: List<String>
    get() = translateData

    fun addLanguagesData(list: List<LanguagesData>?){

        list?.let { languagesDataList.addAll(it) }

    }

    fun addDetectData(list: List<DetectData>?){
        detectDataList.clear()
        list?.forEach {
            detectDataList.add(it)
        }
    }

    fun addTranslateData(list: String?){
        translateData.clear()
        list?.let { translateData.add(it) }

    }

    object Setting {
        var inputText: String = "book"
        var inputTextDetect: String = "hello"
        var inputLanguage: String = "en"
        var outputLanguage: String = "es"

    }
}