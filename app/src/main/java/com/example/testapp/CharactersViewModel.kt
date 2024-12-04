package com.example.testapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).addTypeConverter(Converters())
        .build()

    private val repository = CharacterRepository(HttpClient(), database.characterDao())

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters

    private val _isOnFirstPage = MutableStateFlow(true)
    val isOnFirstPage: StateFlow<Boolean> = _isOnFirstPage
    private val _isOnLastPage = MutableStateFlow(false)
    val isOnLastPage: StateFlow<Boolean> = _isOnLastPage

    private val _currentPageFlow = MutableStateFlow(1)
    val currentPageFlow: StateFlow<Int> = _currentPageFlow

    internal var currentPage = 1
    private val pageSize = 50

    // Проверяет, пуста ли локальная база данных
    suspend fun checkLocalDatabase(): Boolean {
        return repository.getCharactersFromDatabase().isNotEmpty()
    }

    // Загружает персонажей из базы данных с учетом пагинации
    fun loadCharactersFromDatabase(page: Int) {
        viewModelScope.launch {
            val cachedCharacters = repository.getCharactersFromDatabase()
            val paginatedCharacters = paginateCharacters(cachedCharacters, page)
            _characters.value = paginatedCharacters
            currentPage = page
            _currentPageFlow.value = page
            updatePageButtonsState(page, cachedCharacters.size)
            Log.d("CharactersViewModel", "Персонажи загружены из локальной базы")
        }
    }

    // Загружает всех персонажей из API и сохраняет их в базу данных
    fun loadAllCharactersFromApi() {
        viewModelScope.launch {
            try {
                clearCache() // Очистка кэша перед загрузкой новых данных
                var page = 1
                var hasMoreData = true
                while (hasMoreData) {
                    val charactersPage = repository.getCharactersFromApi(page, pageSize)
                    if (charactersPage.isEmpty()) {
                        hasMoreData = false
                    } else {
                        repository.saveCharactersToDatabase(charactersPage)
                        page++
                    }
                }
                Log.d("CharactersViewModel", "Персонажи загружены из API и сохранены в локальную базу")

                // После загрузки данных из API подгружаем их с пагинацией
                currentPage = 1
                _currentPageFlow.value = currentPage
                loadCharactersFromDatabase(currentPage) // Подгружаем первую страницу

            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Ошибка при загрузке персонажей из API: ${e.message}")
            }
        }
    }

    // Разделяет данные на страницы
    private fun paginateCharacters(
        characters: List<CharacterEntity>,
        page: Int
    ): List<CharacterEntity> {
        val fromIndex = (page - 1) * pageSize
        val toIndex = minOf(fromIndex + pageSize, characters.size)
        return if (fromIndex < characters.size) characters.subList(fromIndex, toIndex) else emptyList()
    }

    // Обновляет данные и кнопки пагинации
    private fun updatePageButtonsState(page: Int, totalItems: Int) {
        val totalPages = (totalItems + pageSize - 1) / pageSize
        _isOnFirstPage.value = page == 1
        _isOnLastPage.value = page == totalPages
    }

    // Очищает кэш в базе данных
    fun clearCache() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }

    suspend fun getTotalPages(): Int {
        val totalItems = repository.getCharactersCountFromDatabase()
        return (totalItems + pageSize - 1) / pageSize
    }
}

