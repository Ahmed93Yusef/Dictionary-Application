package com.example.dictionaryapplication.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dictionaryapplication.R
import com.example.dictionaryapplication.data.Status
import com.example.dictionaryapplication.data.network.Client
import com.example.dictionaryapplication.data.repository.DictionaryRepository
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputLanguage
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.inputTextDetect
import com.example.dictionaryapplication.data.repository.DictionaryRepository.Setting.outputLanguage
import com.example.dictionaryapplication.data.repository.DictionaryRepository.detectList
import com.example.dictionaryapplication.data.repository.DictionaryRepository.languageList
import com.example.dictionaryapplication.data.repository.DictionaryRepository.translateText
import com.example.dictionaryapplication.data.response.detect.DetectData
import com.example.dictionaryapplication.data.response.languages.LanguagesData
import com.example.dictionaryapplication.data.response.translate.TranslateData
import com.example.dictionaryapplication.databinding.ActivityHomeBinding
import com.example.dictionaryapplication.util.Constant.TAG
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    @DelicateCoroutinesApi
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

    @DelicateCoroutinesApi
    private fun callBack() {
        binding.getLanguageButton.setOnClickListener {
                getText()
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
    private suspend fun onLanguageResponse(response: Status<List<LanguagesData>>) {

        when (response) {
            is Status.Fail -> {
                Log.i(TAG,"error: ${response.message}")
            }
            Status.Loading -> {
                Log.i(TAG,"loading")
            }
            is Status.Success -> {
                response.data.let { DictionaryRepository.addLanguagesData(it) }
                Log.i(TAG, languageList.joinToString { it.name.toString() })

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
            is Status.Fail -> {
                Log.i(TAG,"error: ${response.message}")
            }
            Status.Loading -> {
                Log.i(TAG,"loading")
            }
            is Status.Success -> {
                response.data.let { DictionaryRepository.addTranslateData(it.translatedText) }
                Log.i(TAG, translateText[0])
                binding.outputText.text = translateText[0]
            }
        }
    }

    private fun getDetectFromRequest() {
        val flow = flow {
            val result = Client.detectRequest()
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
                response.data.let { DictionaryRepository.addDetectData(it) }
                Log.i(TAG, detectList.map { it.language }.toString())
                languageList.forEach { it1 ->
                    val text = detectList.map { it.language }
                    if (it1.code == text[0]){
                        binding.outputText.text = it1.name
                    }
                }
            }
        }
    }

    private fun setSpinnerState(){
        val item = languageList.map { it.name }
        val adapter = ArrayAdapter(this, R.layout.list_item, item)
        (binding.inputSpinner.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.outputSpinner.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.setInputSpinner.setOnItemClickListener { _, _, i, _ ->
            inputLanguage = requireNotNull(languageList[i].code)
        }
        binding.setOutputSpinner.setOnItemClickListener { _, _, i, _ ->
            outputLanguage = requireNotNull(languageList[i].code)
        }
    }
    private fun getText(){
//        val edit = binding.inputText.toString()
//        inputText = edit
//        getTranslateFromRequest()

        val detectEdit = binding.detectInputText.text
        inputTextDetect = detectEdit.toString()
        Log.i(TAG, inputTextDetect)
        getDetectFromRequest()
    }
}