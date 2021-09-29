package com.example.dictionaryapplication.data.network

import com.example.dictionaryapplication.data.Status
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputLanguage
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputText
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.outputLanguage
import com.example.dictionaryapplication.data.detect.DetectData
import com.example.dictionaryapplication.data.response.languages.LanguagesData
import com.example.dictionaryapplication.data.response.translate.TranslateData
import com.example.dictionaryapplication.util.MyUrl.myDetectUrl
import com.example.dictionaryapplication.util.MyUrl.myLanguagesUrl
import com.example.dictionaryapplication.util.MyUrl.myTranslateUrl
import com.example.dictionaryapplication.util.Parameters.INPUT_LANGUAGE
import com.example.dictionaryapplication.util.Parameters.INPUT_TEXT
import com.example.dictionaryapplication.util.Parameters.OUTPUT_LANGUAGE
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

object Client {
    private val okHttp = OkHttpClient()
    private val gson = Gson()

    fun initLanguagesRequest(): Status<List<LanguagesData>>{
        val request = Request.Builder().url(myLanguagesUrl).build()
        val response = okHttp.newCall(request).execute()
        return if (response.isSuccessful){
            val result = gson.fromJson(response.body?.string(), Array<LanguagesData>::class.java).toList()
            Status.Success(result)
        } else {
            Status.Fail(response.message)
        }
    }

    fun translateRequest(): Status<TranslateData>{
        val formBody = FormBody.Builder()
            .add(INPUT_TEXT , inputText)
            .add(INPUT_LANGUAGE , inputLanguage)
            .add(OUTPUT_LANGUAGE , outputLanguage)
            .build()
        val request = Request.Builder().url(myTranslateUrl).post(formBody).build()
        val response = okHttp.newCall(request).execute()

        return if (response.isSuccessful){
            val result = gson.fromJson(response.body?.string(), TranslateData::class.java)
                Status.Success(result)
        } else {
            Status.Fail(response.message)
        }
    }

    fun detectRequest(inputTextDetect: String): Status<List<DetectData>>{
        val formBody = FormBody.Builder()
            .add(INPUT_TEXT ,inputTextDetect)
            .build()
        val request = Request.Builder().url(myDetectUrl).post(formBody).build()
        val response = okHttp.newCall(request).execute()
        return if (response.isSuccessful){
            val result = gson.fromJson(response.body?.string(), Array<DetectData>::class.java).toList()
            Status.Success(result)
        } else {
            Status.Fail(response.message)
        }
    }

}