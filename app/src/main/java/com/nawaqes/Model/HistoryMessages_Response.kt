package com.nawaqes.Model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class HistoryMessages_Response(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("shop")
        val shop: Shop
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Shop(
            @SerializedName("category_id")
            val categoryId: String,
            @SerializedName("city_id")
            val cityId: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("image")
            val image: String,
            @SerializedName("lat")
            val lat: String,
            @SerializedName("lng")
            val lng: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("address")
            val address: String,

            @SerializedName("phone")
            val phone: String,
            @SerializedName("premium")
            val premium: String,
            @SerializedName("state_id")
            val stateId: String
        ) : Parcelable
    }
}