package com.example.citate_of_day.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citate_of_day.data.Quote
import com.example.citate_of_day.data.RetrofitInstance
import com.example.citate_of_day.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val repository: QuoteRepository = QuoteRepository(RetrofitInstance.api),
    requestInterval: Long = 1000L,
    autoLoadInit: Boolean = true
) :
    ViewModel() {

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var lastRequestTime = 0L
    private val REQUEST_INTERVAL = requestInterval

    init {
        if (autoLoadInit) loadRandomQuote()
    }

    fun loadRandomQuote() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastRequestTime < REQUEST_INTERVAL) {
            _quote.value = Quote(
                q = "Please, wait a few time before a new quote",
                a = "System",
            )
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getRandomQuote()
                _quote.value = result
                lastRequestTime = System.currentTimeMillis() // Обновляем время последнего запроса
                Log.d("QuoteViewModel", result.toString())
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Error loading: $e")
                if (e is retrofit2.HttpException && e.code() == 429) {
                    _quote.value = Quote(
                        q = "The limit of requests to the server has been exceeded. Please try again later..",
                        a = "System"
                    )
                } else {
                    _quote.value = Quote(
                        q = "An error occurred when uploading a quote. Please try again later. $e",
                        a = "System"
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
