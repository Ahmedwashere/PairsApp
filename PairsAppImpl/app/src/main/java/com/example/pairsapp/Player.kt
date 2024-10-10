package com.example.pairsapp

data class Player(
    val email: String,
    val firstName: String,
    val lastName: String,
    val skillLevel: Int
)

enum class SkillLevel {
    NOVICE,COMPETENT, EXPERT, UNKNOWN
}