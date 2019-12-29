package com.nawaqes.Model

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

class PlaceInfo {

    private var name: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var id: String? = null
    private var website: Uri? = null
    private var latlong: LatLng? = null
    private var rating: Float = 0.toFloat()
    private var attruibtion: String? = null
    fun PlaceInfo() {}

    fun PlaceInfo(
        name: String,
        address: String,
        phoneNumber: String,
        id: String,
        website: Uri,
        latlong: LatLng,
        rating: Float,
        attruibtion: String
    ) {
        this.name = name
        this.address = address
        this.phoneNumber = phoneNumber
        this.id = id
        this.website = website
        this.latlong = latlong
        this.rating = rating
        this.attruibtion = attruibtion
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getAddress(): String? {
        return address
    }

    fun setAddress(address: String) {
        this.address = address
    }

    fun getPhoneNumber(): String? {
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getWebsite(): Uri? {
        return website
    }

    fun setWebsite(website: Uri) {
        this.website = website
    }

    fun getLatlong(): LatLng? {
        return latlong
    }

    fun setLatlong(latlong: LatLng) {
        this.latlong = latlong
    }

    fun getRating(): Float {
        return rating
    }

    fun setRating(rating: Float) {
        this.rating = rating
    }

    fun getAttruibtion(): String? {
        return attruibtion
    }

    fun setAttruibtion(attruibtion: String) {
        this.attruibtion = attruibtion
    }

    override fun toString(): String {
        return "PlaceInfo{" +
                "name='" + name + '\''.toString() +
                ", address='" + address + '\''.toString() +
                ", phoneNumber='" + phoneNumber + '\''.toString() +
                ", id='" + id + '\''.toString() +
                ", website=" + website +
                ", latlong=" + latlong +
                ", rating=" + rating +
                ", attruibtion='" + attruibtion + '\''.toString() +
                '}'.toString()
    }
}