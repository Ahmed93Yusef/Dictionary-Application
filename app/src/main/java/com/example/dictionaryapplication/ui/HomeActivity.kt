package com.example.dictionaryapplication.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dictionaryapplication.R
import com.example.dictionaryapplication.data.Status
import com.example.dictionaryapplication.data.response.detect.DetectData
import com.example.dictionaryapplication.data.network.Client
import com.example.dictionaryapplication.data.DataManager
import com.example.dictionaryapplication.data.DataManager.Setting.detectText
import com.example.dictionaryapplication.data.DataManager.Setting.inputLanguage
import com.example.dictionaryapplication.data.DataManager.Setting.inputText
import com.example.dictionaryapplication.data.DataManager.Setting.outputLanguage
import com.example.dictionaryapplication.data.DataManager.Setting.textTranslate
import com.example.dictionaryapplication.data.DataManager.languageOutputList
import com.example.dictionaryapplication.data.response.languages.LanguagesData
import com.example.dictionaryapplication.data.response.translate.TranslateData
import com.example.dictionaryapplication.databinding.ActivityHomeBinding
import com.example.dictionaryapplication.util.Constant.TAG
import com.example.dictionaryapplication.util.slideVisibility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
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
        setOnClickSpinner()
    }

    /**
     * get language request by using flow.
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun getLanguageFromRequest() {
        val flow = flow {
            emit(Status.Loading)
            emit(Client.initLanguagesRequest())
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "language fail: ${it.message}")
            }.collect {
                onLanguageResponse(it)
            }
        }
    }

    /**
     * set state of language response
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun onLanguageResponse(response: Status<List<LanguagesData>>) {
        val itemList = mutableListOf(
            binding.detectText,
            binding.getLanguageButton,
            binding.outputText,
            binding.outputSpinner,
            binding.inputSpinner,
            binding.inputCard,
            binding.outputCard,
            binding.inputImage,
            binding.outputImage,
            binding.inputText,
        )
        when (response) {
            is Status.Fail -> {
                itemList.forEach {
                    it.slideVisibility(false)
                }
                binding.apply {
                    blueView.isVisible = false
                    translatorText.isVisible = false
                    loading.apply {
                        slideVisibility(visibility = true)
                        setAnimation(R.raw.no_connection)
                        playAnimation()
                    }
                }

                Log.i(TAG, "language error: ${response.message}")
            }
            Status.Loading -> {
                itemList.forEach {
                    it.slideVisibility(false)
                }
                binding.apply {
                    blueView.isVisible = false
                    translatorText.isVisible = false
                    loading.apply {
                        slideVisibility(visibility = true)
                        setAnimation(R.raw.loading)
                        playAnimation()
                    }
                }
                Log.i(TAG, "language loading")
            }
            is Status.Success -> {
                binding.apply {
                    blueView.isVisible = true
                    translatorText.isVisible = true
                    loading.slideVisibility(visibility = false)
                }
                itemList.forEach {
                    it.slideVisibility(true)
                }
                response.data.let { DataManager.addLanguagesData(it) }
                Log.i(TAG, "list of language : $languageOutputList")

            }
        }
        setSpinnerState()
    }

    /**
     * get language request by using flow.
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun getTranslateFromRequest() {
        val flow = flow {
            val result = Client.translateRequest()
            emit(result)
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "translate fail: ${it.message}")
            }.collect {
                onTranslateResponse(it)
            }
        }
    }

    /**
     * set state of translate response
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun onTranslateResponse(response: Status<TranslateData>) {
        when (response) {
            is Status.Fail -> Log.i(TAG, "translate error: ${response.message}")
            Status.Loading -> Log.i(TAG, "translate loading")
            is Status.Success -> {
                textTranslate = response.data.let { it.translatedText.toString() }
                Log.i(TAG, "text translated : $textTranslate")
                binding.outputText.text = textTranslate
            }
        }
    }

    /**
     * get language request by using flow.
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun getDetectFromRequest(code: String) {
        val flow = flow {
            val result = Client.detectRequest(code)
            emit(result)
        }.flowOn(Dispatchers.Default)
        lifecycleScope.launch {
            flow.catch {
                Log.i(TAG, "detect fail: ${it.message}")
            }.collect {
                onDetectResponse(it)
            }
        }
    }

    /**
     * set state of detect response
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun onDetectResponse(response: Status<List<DetectData>>) {
        when (response) {
            is Status.Fail -> {
                Log.i(TAG, "detect error: ${response.message}")
            }
            Status.Loading -> {
                Log.i(TAG, "detect loading")
            }
            is Status.Success -> {
                detectText = response.data[0].language.toString()
                inputLanguage = detectText
                Log.i(TAG, "detect language: $detectText")
                imageFlagRequest(detectText, binding.inputImage)
                languageOutputList.forEach {
                    if (it.code == detectText) {
                        binding.detectText.text = it.name
                    }
                }
                getTranslateFromRequest()
            }
        }
    }

    /**
     * set spinner state
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun setSpinnerState() {
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

    /**
     * loading image by using glide library
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun imageFlagRequest(flagCode: String, view: ImageView) {
        var countryCode = flagCode
        when (countryCode) {
            "en" -> {
                countryCode = "gb"
            }
            "ar" -> {
                countryCode = "iq"
            }
            "ja" -> {
                countryCode = "jp"
            }
            "ko" -> {
                countryCode = "kr"
            }
            "zh" -> {
                countryCode = "cn"
            }
            "hi" -> {
                countryCode = "in"
            }
            "vi" -> {
                countryCode = "vn"
            }
        }
        val url = "https://www.countryflags.io/$countryCode/flat/64.png"
        Glide.with(this).load(url)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .error(R.drawable.ic_baseline_error)
            .into(view)
    }

    /**
     * set on click of spinner items.
     * @author     Ahmed Yusef
     * @return     Unit
     * */
    private fun setOnClickSpinner() {
        binding.setInputSpinner.setOnItemClickListener { _, _, i, _ ->
            if (i != 0) {
                binding.detectText.isVisible = false
                val code = languageOutputList[i - 1].code
                inputLanguage = requireNotNull(code)
                imageFlagRequest(code, binding.inputImage)
                binding.getLanguageButton.setOnClickListener {
                    inputText = binding.inputText.editText?.text.toString()
                    getTranslateFromRequest()
                }
            } else {
                binding.detectText.isVisible = true
                binding.getLanguageButton.setOnClickListener {
                    inputText = binding.inputText.editText?.text.toString()
                    getDetectFromRequest(inputText)
                }
            }
        }
        binding.setOutputSpinner.setOnItemClickListener { _, _, i, _ ->
            val code = languageOutputList[i].code
            outputLanguage = requireNotNull(code)
            imageFlagRequest(code, binding.outputImage)
        }
    }
}