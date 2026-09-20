package com.norskallstars.data.model

import com.google.gson.annotations.SerializedName

data class SentenceData(
    @SerializedName("norwegianWord") val norwegianWord: String, // Слово на норвежском для связи
    @SerializedName("norwegian") val norwegian: String, // Предложение на норвежском
    @SerializedName("russian") val russian: String // Перевод предложения
)