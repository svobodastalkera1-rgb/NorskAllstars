package com.norskallstars.data.model

import com.google.gson.annotations.SerializedName

data class SentencesJson(
    @SerializedName("sentences") val sentences: List<SentenceData>
)