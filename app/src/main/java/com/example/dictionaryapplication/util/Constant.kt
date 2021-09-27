package com.example.dictionaryapplication.util

import okhttp3.HttpUrl

object Constant {
    const val SCHEME = "https"
    const val HOST = "translate.argosopentech.com"
    const val LANGUAGES_PATH = "languages"
    const val TRANSLATE_PATH = "translate"
    const val DETECT_PATH = "detect"
    const val TEXT = "text"
    const val TAG = "MAIN_ACTIVITY"
}
object Parameters {
    const val INPUT_TEXT = "q"
    const val INPUT_LANGUAGE = "source"
    const val OUTPUT_LANGUAGE = "target"
    const val OUTPUT_FORMAT = "format"
}
object MyUrl {
    val myLanguagesUrl = HttpUrl.Builder()
        .scheme(Constant.SCHEME)
        .host(Constant.HOST)
        .addPathSegment(Constant.LANGUAGES_PATH)
        .build()
    val myTranslateUrl = HttpUrl.Builder()
        .scheme(Constant.SCHEME)
        .host(Constant.HOST)
        .addPathSegment(Constant.TRANSLATE_PATH)
        .build()
    val myDetectUrl = HttpUrl.Builder()
        .scheme(Constant.SCHEME)
        .host(Constant.HOST)
        .addPathSegment(Constant.DETECT_PATH)
        .build()
}