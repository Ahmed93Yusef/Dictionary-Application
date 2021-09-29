package com.example.dictionaryapplication.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dictionaryapplication.R
import com.example.dictionaryapplication.data.Status
import com.example.dictionaryapplication.data.network.Client
import com.example.dictionaryapplication.data.repository.DictionaryRepository
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputLanguage
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputText
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.outputLanguage
import com.example.dictionaryapplication.data.repository.DictionaryRepository.languageOutputList
import com.example.dictionaryapplication.data.repository.DictionaryRepository.translateText
import com.example.dictionaryapplication.data.detect.DetectData
import com.example.dictionaryapplication.data.response.languages.LanguagesData
import com.example.dictionaryapplication.data.response.translate.TranslateData
import com.example.dictionaryapplication.databinding.ActivityHomeBinding
import com.example.dictionaryapplication.util.Constant.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.*

class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private var detectText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        callBack()
    }
    private fun setup() {
        getLanguageFromRequest()
    }

    private fun callBack() {
        binding.setInputSpinner.setOnItemClickListener { _, _, i, _ ->
            if (i != 0) {
                val code = languageOutputList[i-1].code
                inputLanguage =  requireNotNull(code)
                imageFlagRequest(code,binding.inputImage)
            } else {
                inputLanguage =  "auto"
                binding.getLanguageButton.setOnClickListener {
                    inputText = binding.inputText.editableText.toString()
                    getDetectFromRequest(inputText)
                    getTranslateFromRequest()
                }
            }
        }
        binding.setOutputSpinner.setOnItemClickListener { _, _, i, _ ->
            val code = languageOutputList[i].code
            outputLanguage = requireNotNull(code)
            imageFlagRequest(code,binding.outputImage)
        }

    }

    private fun getLanguageFromRequest() {
        val flow = flow {
            val result = Client.initLanguagesRequest()
            emit(result)
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "fail: ${it.message}")
            }.collect {
                onLanguageResponse(it)
            }
        }
    }
    private fun onLanguageResponse(response: Status<List<LanguagesData>>) {

        when (response) {
            is Status.Fail -> {
                Log.i(TAG,"error: ${response.message}")
            }
            Status.Loading -> {
                Log.i(TAG,"loading")
            }
            is Status.Success -> {
                response.data.let { DictionaryRepository.addLanguagesData(it) }
                Log.i(TAG, languageOutputList.toString())

            }
        }
        setSpinnerState()
    }

    private fun getTranslateFromRequest() {
        val flow = flow {
            val result = Client.translateRequest()
            emit(result)
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "fail: ${it.message}")
            }.collect {
                onTranslateResponse(it)
            }
        }
    }
    private fun onTranslateResponse(response: Status<TranslateData>) {
        when (response) {
            is Status.Fail -> Log.i(TAG,"error: ${response.message}")
            Status.Loading -> Log.i(TAG,"loading")
            is Status.Success -> {
                response.data.let { DictionaryRepository.addTranslateData(it.translatedText) }
                Log.i(TAG, translateText[0])
                binding.outputText.text = translateText[0]
            }
        }
    }
    private fun getDetectFromRequest(code: String) {
        val flow = flow {
            val result = Client.detectRequest(code)
            emit(result)
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "fail: ${it.message}")
            }.collect {
                onDetectResponse(it)
            }
        }
    }
    private fun onDetectResponse(response: Status<List<DetectData>>) {
        when (response) {
            is Status.Fail -> {
                Log.i(TAG,"error: ${response.message}")
            }
            Status.Loading -> {
                Log.i(TAG,"loading")
            }
            is Status.Success -> {
                detectText = ""
                detectText = response.data[0].language.toString()
                Log.i(TAG,detectText)
                imageFlagRequest(detectText,binding.inputImage)
                }

        }
    }

    private fun setSpinnerState(){
        val inputItem = mutableListOf("auto")
        languageOutputList.forEach {
            inputItem.add(it.name.toString())
        }
        val outputItem = languageOutputList.map { it.name }
        val inputAdapter = ArrayAdapter(this, R.layout.list_item, inputItem)
        val outputAdapter = ArrayAdapter(this, R.layout.list_item, outputItem)
        (binding.inputSpinner.editText as? AutoCompleteTextView)?.setAdapter(inputAdapter)
        (binding.outputSpinner.editText as? AutoCompleteTextView)?.setAdapter(outputAdapter)
    }
    private fun imageFlagRequest(flagCode: String, view: ImageView){
        var countryCode = flagCode
        var url = ""
        when (countryCode) {
            "en" -> {
                countryCode = "gb"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "ar" -> {
                countryCode = "iq"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "ja" -> {
                countryCode = "jp"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "ko" -> {
                countryCode = "kr"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "zh" -> {
                countryCode = "cn"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "hi" -> {
                countryCode = "in"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            "vi" -> {
                countryCode = "vn"
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
            else -> {
                url = "https://www.countryflags.io/$countryCode/flat/64.png"
            }
        }
            Glide.with(this).load(url)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_error)
                .into(view)

    }
}