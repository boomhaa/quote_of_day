package com.example.citate_of_day.repository

import com.example.citate_of_day.data.Quote
import com.example.citate_of_day.data.QuoteApi

open class QuoteRepository (
    private val api: QuoteApi
){
    suspend fun getRandomQuote():Quote{
        val quotes = api.getQuotes()
        return if (quotes.isNullOrEmpty()) Quote("No quotes available", "System")
        else quotes.random()
    }
}