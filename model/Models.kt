package com.vibelocal.app.model

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val fullName: String, val email: String, val password: String, val location: String? = null)
data class AuthResponse(val userId: Int, val fullName: String, val email: String, val token: String)
data class EventDto(val eventId: Int, val title: String, val description: String, val dateTime: String, val location: String, val latitude: Double, val longitude: Double, val category: String, val price: Double, val vibeMatch: Int? = null)
data class PreferenceDto(val language: String, val theme: String, val interests: List<String>, val location: String?)
data class NotificationDto(val notificationId: Int, val message: String, val read: Boolean)
data class ApiMessage(val message: String)
