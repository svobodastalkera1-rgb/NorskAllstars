package com.norskallstars.data.model

import com.google.gson.annotations.SerializedName

data class WordData(
    @SerializedName("norwegian") val norwegian: String,
    @SerializedName("russian") val russian: String,
    @SerializedName("partOfSpeech") val partOfSpeech: String,
    @SerializedName("difficulty") val difficulty: String
)