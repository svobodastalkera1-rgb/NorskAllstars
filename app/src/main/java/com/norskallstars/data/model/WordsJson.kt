package com.norskallstars.data.model

import com.google.gson.annotations.SerializedName

data class WordsJson(
    @SerializedName("words") val words: List<WordData>
)