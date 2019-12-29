package com.nawaqes.Retrofit

import com.nawaqes.Model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @POST("customer/signup")
     fun userRegister(
        @QueryMap map:Map<String,String>): Call<Register_Model>
//
    @POST("customer/login")
    fun userLogin(
        @QueryMap map:Map<String,String>): Call<Register_Model>



    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/cities")
    fun getCities(@Header("Authorization")auth:String): Call<Cities_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/categories")
    fun getCategories(@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Categories_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("customer/user")
    fun getProfile(@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Profile_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/banners")
    fun getBanners(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Banners_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/countNewMessages")
    fun getNewMessage(@Header("Authorization")auth:String): Call<CountNotifications_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/countNewNotifications")
    fun getNewNotifictions(@Header("Authorization")auth:String): Call<CountNotifications_Response>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/searchCategory")
    fun getSearchCategories(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Categories_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/searchSubCategory")
    fun SearchSubCategories(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<SubCategories_Response>




    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/subcategories")
    fun getSubCategories(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<SubCategories_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/offers")
    fun getOffers(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Offers_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/customer_inboxes")
    fun getLastMessage(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String): Call<Inbox_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/inbox_history")
    fun getHistoryMessage(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String): Call<HistoryMessages_Response>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/CustomershortLists")
    fun getRequests(@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<Requests_Shortlists_Response>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/get_shops_that_accept_the_shortlist")
    fun getCustomRequests(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String,@Header("X-localization")lang:String): Call<DetailsShortlists_Response>


    @Multipart
    @POST("customer/addShortList")
    fun AddRequest(@Part img:MultipartBody.Part?, @Part("description") description:RequestBody
                   ,@Part("city_id") city_id:RequestBody,
                   @Part("state_id") state_id:RequestBody,@Part("lat") lat:RequestBody,
    @Part("lng") lng:RequestBody,@Part("category_id") category_id:RequestBody,
    @Part("subcategory_id") subcategory_id:RequestBody?
    , @Header("Authorization")auth:String, @Header("X-localization")lang:String): Call<AddRequest_Response>

    @Multipart
    @POST("customer/addCustomerShopInbox")
    fun SentMessage(@Part img:MultipartBody.Part?, @Part("message") description:RequestBody,@Part("shop_id") id:RequestBody,
                 @Header("Authorization")auth:String): Call<SentMessage_Response>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("customer/states")
    fun getStates(@QueryMap map:Map<String,String>,@Header("Authorization")auth:String): Call<Cities_Response>

}