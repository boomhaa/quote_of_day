package com.example.citate_of_day.data

import com.google.gson.annotations.SerializedName


data class Quote(
    @SerializedName("q")
    val q: String,

    @SerializedName("a")
    val a: String
)