package com.clinicscluster.helper

import com.clinicscluster.helper.ApiContants
import com.clinicscluster.model.appointment.AppointmentModel
import com.clinicscluster.model.dashboard.DashboardModel
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @get:GET(ApiContants.PREF_homepage)
    val dashBoardData: Call<DashboardModel>

    @get:GET(ApiContants.PREF_front_service)
    val frontService: Call<JsonObject>

    @get:GET(ApiContants.PREF_get_clinics)
    val clinic: Call<JsonObject>

    @get:GET(ApiContants.PREF_services)
    val services: Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiContants.PREF_getdoctorsbyclinic)
    fun getdoctorsbyclinic(@Field("clinic_id") clinic_id: Int): Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiContants.PREF_getslotbydoctor)
    fun getslotbydoctor(@Field("doctor_id") doctor_id: Int, @Field("appointment_date") appointment_date: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiContants.PREF_login)
    fun login(
        @Field("email") email: String,
        @Field("password") password: String, @Field("device_token") device_token: String
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST(ApiContants.PREF_add_appointments)
    fun add_appointments(
        @Field("user_id") user_id: String,
        @Field("appointment_date") appointment_date: String,
        @Field("clinic_id") clinic_id: Int,
        @Field("doctor_id") doctor_id: Int,
        @Field("services") services: String,
        @Field("time_slot") time_slot: String,
        @Field("description") description: String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiContants.PREF_forget_password)
    fun forget_password(@Field("email") email: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiContants.PREF_my_appointments)
    fun myAppointments(@Field("user_id") email: Int): Call<AppointmentModel>

    @Multipart
    @POST(ApiContants.PREF_userregister)
    fun userregister(
        @Part("first_name")first_name: RequestBody?,
        @Part("last_name")last_name: RequestBody?,
        @Part("email")email: RequestBody?,
        @Part("password")password: RequestBody?,
        @Part("phone")phone: RequestBody?,
        @Part("dob")subject: RequestBody?,
        @Part("blood_group")title: RequestBody?,
        @Part("clinic_id")description: RequestBody?,
        @Part("gender")submission_date: RequestBody?,
        @Part("pincode")pincode: RequestBody?,
        @Part("address")address: RequestBody?,
        @Part("country")country: RequestBody?,
        @Part("city")city: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Call<JsonObject>


    @GET("photos")
    fun demo(): Call<JsonArray?>?
}