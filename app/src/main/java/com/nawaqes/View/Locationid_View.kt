package com.nawaqes.View

import com.nawaqes.Model.Categories_Response

interface Locationid_View {

    fun CityId(id:String,Name:String)
    fun Areaid(id:String,Name:String)
    fun Cat_id(CatId:Int,categories :Int,cat_name:String)
    
}