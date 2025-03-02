package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Student(val id: Int? = null, val name: String, val attended: Boolean)
