package com.example.appstarwars

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character_table")
    fun getAllCharacters(): LiveData<List<CharacterEntity>>

    @Query("SELECT * FROM character_table WHERE name LIKE :searchQuery")
    fun searchCharacters(searchQuery: String): LiveData<List<CharacterEntity>>

    @Query("DELETE FROM character_table")
    suspend fun deleteAllCharacters()
}
