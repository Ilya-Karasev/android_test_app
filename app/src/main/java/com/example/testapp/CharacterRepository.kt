package com.example.testapp

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val name: String,
    val culture: String,
    val born: String,
    val titles: List<String>,
    val aliases: List<String>,
    val playedBy: List<String>
)

class CharacterRepository(
    private val client: HttpClient,
    private val characterDao: CharacterDao
) {
    private val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

    suspend fun getCharactersFromApi(page: Int, pageSize: Int): List<CharacterEntity> {
        val response: HttpResponse = client.get("https://www.anapioficeandfire.com/api/characters?page=$page&pageSize=$pageSize")
        val jsonString = response.bodyAsText()
        return json.decodeFromString<List<Character>>(jsonString)
            .map { it.toEntity() }
    }

    suspend fun saveCharactersToDatabase(characters: List<CharacterEntity>) {
        withContext(Dispatchers.IO) {
            characterDao.insertCharacters(characters)
        }
    }

    suspend fun getCharactersFromDatabase(): List<CharacterEntity> {
        return withContext(Dispatchers.IO) {
            characterDao.getAllCharacters()
        }
    }

    suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            characterDao.deleteAllCharacters()
        }
    }

    suspend fun getCharactersCountFromDatabase(): Int {
        return withContext(Dispatchers.IO) {
            characterDao.getCharactersCount()
        }
    }
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        name = name,
        culture = culture,
        born = born,
        titles = titles,
        aliases = aliases,
        playedBy = playedBy
    )
}
