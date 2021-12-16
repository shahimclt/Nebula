package com.example.nebula.data.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

data class ImageObject (
    val copyright: String?,
    @SerializedName("date") private val _date: String?,
    val explanation: String?,
    @SerializedName("hdurl") val hdUrl: String?,
    @SerializedName("media_type") val mediaType: String?,
    val title: String,
    val url: String) {

    var aspectRatio: Float? = null

    val safeAspectRatio : String
        get() {
            return String.format("%.2f", aspectRatio?:1f)
        }

    val hasAspectRatio : Boolean
        get() {
            return aspectRatio != null
        }

    val date : LocalDate
    get() {
        return LocalDate.parse(_date)
    }
}
