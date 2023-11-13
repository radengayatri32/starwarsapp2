package com.example.appstarwars


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val gender: String?,
    val birthYear: String?,
    val skinColor: String?,
    val eyeColor: String?,
    val height: String?
)
