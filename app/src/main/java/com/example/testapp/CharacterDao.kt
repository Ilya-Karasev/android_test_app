package com.example.testapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharactersFlow(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    fun deleteAllCharacters()

    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun getCharacterById(characterId: Int): CharacterEntity?

    @Query("""
        UPDATE characters 
        SET name = :name, 
            culture = :culture, 
            born = :born, 
            titles = :titles, 
            aliases = :aliases, 
            playedBy = :playedBy 
        WHERE id = :characterId
    """)
    fun updateCharacterById(
        characterId: Int,
        name: String,
        culture: String?,
        born: String?,
        titles: List<String>?,
        aliases: List<String>?,
        playedBy: List<String>?
    )

    @Query("SELECT COUNT(*) FROM characters")
    fun getCharactersCount(): Int
}

