package com.example.testapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.example.testapp.User
import com.example.testapp.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.example.testapp.dataStore
import android.os.Environment
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.room.Room
import com.example.testapp.AppDatabase
import com.example.testapp.CharactersViewModel
import com.example.testapp.Converters
import java.io.File
import java.io.IOException

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")
    private val TAG = "SettingsFragment"
    private val viewModel: CharactersViewModel by viewModels()

    // Ключи для Data Store
    private val THEME_KEY = stringPreferencesKey("theme")
    private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")

    // Ключи для Shared Preferences
    private val PREF_NAME = "user_settings"
    private val LANGUAGE_KEY = "language"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var user: User

    // Переменные для временного хранения настроек
    private var selectedTheme: String = "system"
    private var notificationsEnabled: Boolean = false
    private var selectedLanguage: String = "ru"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val args = SettingsFragmentArgs.fromBundle(requireArguments())
        user = args.user

        setupListeners()
        loadSettings()
        checkFilePresence() // Проверка файла при создании View

        return binding.root
    }

    private fun setupListeners() {
        // Обработчики для временного сохранения значений настроек
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            selectedTheme = when (checkedId) {
                R.id.radio_button_light -> "light"
                R.id.radio_button_dark -> "dark"
                else -> "system"
            }
        }

        binding.toggleNotifications.setOnCheckedChangeListener { _, isChecked ->
            notificationsEnabled = isChecked
            updateNotificationToggleColor(isChecked)
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            selectedLanguage = when (checkedId) {
                R.id.radio_button_ru -> "ru"
                else -> "en"
            }
        }

        // Сохранение настроек при нажатии на кнопку изменения
        binding.btnChangeSettings.setOnClickListener {
            saveSettings()
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment(user))
        }

        // Возврат без сохранения
        binding.tvBack.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment(user))
        }
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            val preferences = requireContext().dataStore.data.first()
            selectedTheme = preferences[THEME_KEY] ?: "system"
            notificationsEnabled = preferences[NOTIFICATIONS_KEY] ?: false

            when (selectedTheme) {
                "light" -> binding.radioButtonLight.isChecked = true
                "dark" -> binding.radioButtonDark.isChecked = true
                else -> binding.radioButtonSystem.isChecked = true
            }

            binding.toggleNotifications.isChecked = notificationsEnabled
            updateNotificationToggleColor(notificationsEnabled)
        }

        selectedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "ru") ?: "ru"
        if (selectedLanguage == "ru") {
            binding.radioButtonRu.isChecked = true
        } else {
            binding.radioButtonEn.isChecked = true
        }
    }

    private fun saveSettings() {
        // Сохранение темы и уведомлений в Data Store
        lifecycleScope.launch {
            requireContext().dataStore.edit { preferences ->
                preferences[THEME_KEY] = selectedTheme
                preferences[NOTIFICATIONS_KEY] = notificationsEnabled
            }
        }
        // Сохранение языка в SharedPreferences
        sharedPreferences.edit().putString(LANGUAGE_KEY, selectedLanguage).apply()
    }

    private fun updateNotificationToggleColor(isEnabled: Boolean) {
        _binding?.let { binding ->
            val color = if (isEnabled) R.color.blue else R.color.gray
            binding.toggleNotifications.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun checkFilePresence() {
        val fileDocuments = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "12.txt")
        val fileDownloads = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "12.txt")

        val fileExists = fileDocuments.exists() || fileDownloads.exists()
        binding.tvFileStatus.text = if (fileExists) {
            "Файл '12.txt' присутствует в системе."
        } else {
            "Файл '12.txt' отсутствует в системе."
        }

        binding.btnDeleteFile.isEnabled = fileExists

        // Проверяем наличие резервной копии во внутреннем хранилище
        val internalBackupFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "12_backup.txt")
        binding.btnRestoreFile.isEnabled = internalBackupFile.exists()

        binding.btnDeleteFile.setOnClickListener {
            if (fileExists) {
                backupFileToInternalStorage(fileDocuments.takeIf { it.exists() } ?: fileDownloads)
                deleteFile(fileDocuments, fileDownloads)
            }
        }

        binding.btnRestoreFile.setOnClickListener {
            restoreFileFromInternalStorage(internalBackupFile)
        }
        binding.btnClearDatabase.setOnClickListener {
            clearDatabase()
        }
    }

    private fun clearDatabase() {
        lifecycleScope.launch {
            try {
                viewModel.clearCache()
                Log.d(TAG, "Локальная база данных успешно очищена.")
                binding.tvFileStatus.text = "Локальная база данных успешно очищена."
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при очистке базы данных: ${e.message}")
                binding.tvFileStatus.text = "Ошибка при очистке базы данных."
            }
        }
    }

    private fun restoreFileFromInternalStorage(internalFile: File) {
        if (internalFile.exists()) {
            try {
                val externalFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "12.txt")
                internalFile.inputStream().use { input ->
                    externalFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                binding.tvFileStatus.text = "Файл '12.txt' успешно восстановлен во внешнем хранилище."
                binding.btnDeleteFile.isEnabled = true
                binding.btnRestoreFile.isEnabled = false
            } catch (e: IOException) {
                Log.e(TAG, "Ошибка при восстановлении файла во внешнее хранилище: ${e.message}")
            }
        }
    }

    private fun backupFileToInternalStorage(sourceFile: File) {
        // Проверяем, существует ли исходный файл
        if (sourceFile.exists()) {
            try {
                val internalFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "12_backup.txt")
                sourceFile.inputStream().use { input ->
                    internalFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d(TAG, "Файл '12.txt' успешно сохранён во внутреннем хранилище.")
            } catch (e: IOException) {
                Log.e(TAG, "Ошибка при копировании файла во внутреннее хранилище: ${e.message}")
            }
        }
    }

    private fun deleteFile(fileDocuments: File, fileDownloads: File) {
        // Удаляем файл, если он существует в одной из директорий
        val fileDeleted = when {
            fileDocuments.exists() -> fileDocuments.delete()
            fileDownloads.exists() -> fileDownloads.delete()
            else -> false
        }

        if (fileDeleted) {
            binding.tvFileStatus.text = "Файл '12.txt' удалён."
        } else {
            binding.tvFileStatus.text = "Файл '12.txt' не удалось удалить."
        }
        // Обновляем состояние кнопки
        binding.btnDeleteFile.isEnabled = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}