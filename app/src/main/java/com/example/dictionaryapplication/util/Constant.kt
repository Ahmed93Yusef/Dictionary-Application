package com.example.dictionaryapplication.util

import com.example.dictionaryapplication.util.Constant.DETECT_PATH
import com.example.dictionaryapplication.util.Constant.HOST
import com.example.dictionaryapplication.util.Constant.LANGUAGES_PATH
import com.example.dictionaryapplication.util.Constant.SCHEME
import com.example.dictionaryapplication.util.Constant.TRANSLATE_PATH
import okhttp3.HttpUrl

object Constant {
    const val SCHEME = "https"
    const val HOST = "translate.argosopentech.com"
    const val LANGUAGES_PATH = "languages"
    const val TRANSLATE_PATH = "translate"
    const val DETECT_PATH = "detect"
    const val TAG = "AHMED_YUSEF"
}

object Parameters {
    const val INPUT_TEXT = "q"
    const val INPUT_LANGUAGE = "source"
    const val OUTPUT_LANGUAGE = "target"
}

object MyUrl {
    val myLanguagesUrl = HttpUrl.Builder()
        .scheme(SCHEME)
        .host(HOST)
        .addPathSegment(LANGUAGES_PATH)
        .build()
    val myTranslateUrl = HttpUrl.Builder()
        .scheme(SCHEME)
        .host(HOST)
        .addPathSegment(TRANSLATE_PATH)
        .build()
    val myDetectUrl = HttpUrl.Builder()
        .scheme(SCHEME)
        .host(HOST)
        .addPathSegment(DETECT_PATH)
        .build()
}