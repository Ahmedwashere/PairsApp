package com.example.pairsapp

data class Player(
    val email: String,
    val firstName: String,
    val lastName: String,
    val skillLevel: SkillLevel
)

enum class SkillLevel {
    NOVICE, COMPETENT, EXPERT, UNKNOWN;

    // Companion Object is the same thing as static in Java.
    companion object {
        fun getSkillLevel(skillLevel: String): SkillLevel {
            return when (skillLevel.uppercase()) {
                "NOVICE" -> NOVICE
                "COMPETENT" -> COMPETENT
                "EXPERT" -> EXPERT
                else -> UNKNOWN

            }
        }
    }
}