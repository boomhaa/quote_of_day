package com.example.citate_of_day.data

import retrofit2.http.GET

interface QuoteApi {
    @GET("api/quotes")
    suspend fun getQuotes(): List<Quote>
}