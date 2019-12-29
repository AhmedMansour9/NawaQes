package com.nawaqes.Model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Requests_Shortlists_Response(
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
        @SerializedName("accepted_by_shop")
        val acceptedByShop: Int,
        @SerializedName("category_name")
        val categoryName: String,
        @SerializedName("city_name")
        val cityName: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("lng")
        val lng: String,
        @SerializedName("state_name")
        val stateName: String,
        @SerializedName("subcategory_name")
        val subcategoryName: String
    ) : Parcelable
}