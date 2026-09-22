package com.vibelocal.app.api

import com.vibelocal.app.model.*
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/register") suspend fun register(@Body body: RegisterRequest): AuthResponse
    @POST("api/auth/login") suspend fun login(@Body body: LoginRequest): AuthResponse
    @GET("api/events") suspend fun events(@Query("userId") userId: Int? = null): List<EventDto>
    @GET("api/events/{id}") suspend fun event(@Path("id") id: Int): EventDto
    @GET("api/events/search") suspend fun search(@Query("keyword") keyword: String? = null, @Query("category") category: String? = null, @Query("date") date: String? = null, @Query("maxPrice") maxPrice: Double? = null, @Query("location") location: String? = null, @Query("latitude") latitude: Double? = null, @Query("longitude") longitude: Double? = null, @Query("distanceKm") distanceKm: Double? = null, @Query("userId") userId: Int? = null): List<EventDto>
    @GET("api/users/{id}/saved") suspend fun saved(@Path("id") userId: Int): List<EventDto>
    @POST("api/users/{id}/saved/{eventId}") suspend fun save(@Path("id") userId: Int, @Path("eventId") eventId: Int): ApiMessage
    @DELETE("api/users/{id}/saved/{eventId}") suspend fun unsave(@Path("id") userId: Int, @Path("eventId") eventId: Int): ApiMessage
    @GET("api/users/{id}/preferences") suspend fun preferences(@Path("id") userId: Int): PreferenceDto
    @PUT("api/users/{id}/preferences") suspend fun updatePreferences(@Path("id") userId: Int, @Body body: PreferenceDto): PreferenceDto
    @GET("api/users/{id}/notifications") suspend fun notifications(@Path("id") userId: Int): List<NotificationDto>
}
