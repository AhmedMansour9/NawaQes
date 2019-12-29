package com.nawaqes.Model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Profile_Response(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("city_id")
        val cityId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String?,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("lng")
        val lng: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("remember_token")
        val rememberToken: String?,
        @SerializedName("social_id")
        val socialId: String?,
        @SerializedName("state_id")
        val stateId: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) : Parcelable
}