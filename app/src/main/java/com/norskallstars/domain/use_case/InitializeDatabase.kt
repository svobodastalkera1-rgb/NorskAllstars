package com.norskallstars.domain.use_case

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.norskallstars.data.local.entities.WordEntity
import com.norskallstars.data.model.SentencesJson
import com.norskallstars.data.model.WordsJson
import com.norskallstars.domain.repository.SentenceRepository
import com.norskallstars.domain.repository.WordRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InitializeDatabase @Inject constructor(
    private val wordRepository: WordRepository,
    private val sentenceRepository: SentenceRepository,
    private val context: Context
) {
    suspend operator fun invoke() {
        try {
            val wordCount = wordRepository.getLearnedWordsCount().first()
            if (wordCount > 0) {
                Log.d("InitializeDatabase", "Database already initialized, skipping")
                return
            }

            Log.d("InitializeDatabase", "Starting database initialization")

            // Очищаем базу перед инициализацией
            wordRepository.deleteAllWords()
            sentenceRepository.deleteAllSentences()

            // Загружаем слова
            if (assetExists(context, "words.json")) {
                val wordsJsonString = try {
                    context.assets.open("words.json").bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    Log.e("InitializeDatabase", "Error reading words.json: ${e.message}")
                    createFallbackData()
                    return
                }

                val wordsData = try {
                    Gson().fromJson(wordsJsonString, WordsJson::class.java)
                } catch (e: JsonSyntaxException) {
                    Log.e("InitializeDatabase", "Error parsing words.json: ${e.message}")
                    createFallbackData()
                    return
                }

                val wordEntities = wordsData.words.mapIndexed { index, wordData ->
                    WordEntity(
                        id = 0, // Автоматическая генерация ID
                        norwegian = wordData.norwegian,
                        russian = wordData.russian,
                        partOfSpeech = wordData.partOfSpeech,
                        difficulty = wordData.difficulty
                    )
                }

                if (wordEntities.isNotEmpty()) {
                    wordRepository.insertAllWords(wordEntities)
                    Log.d("InitializeDatabase", "Inserted ${wordEntities.size} words")
                }
            }

            // Загружаем предложения
            if (assetExists(context, "sentences.json")) {
                val sentencesJsonString = try {
                    context.assets.open("sentences.json").bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    Log.e("InitializeDatabase", "Error reading sentences.json: ${e.message}")
                    return
                }

                val sentencesData = try {
                    Gson().fromJson(sentencesJsonString, SentencesJson::class.java)
                } catch (e: JsonSyntaxException) {
                    Log.e("InitializeDatabase", "Error parsing sentences.json: ${e.message}")
                    return
                }

                // Получаем все слова для сопоставления ID
                val allWords = wordRepository.getAllWords().first()

                val sentenceEntities = sentencesData.sentences.flatMap { sentenceData ->
                    allWords.filter { it.norwegian == sentenceData.norwegianWord }
                        .map { word ->
                            com.norskallstars.data.local.entities.SentenceEntity(
                                id = 0, // Автоматическая генерация ID
                                wordId = word.id,
                                norwegian = sentenceData.norwegian,
                                russian = sentenceData.russian
                            )
                        }
                }

                if (sentenceEntities.isNotEmpty()) {
                    sentenceRepository.insertAllSentences(sentenceEntities)
                    Log.d("InitializeDatabase", "Inserted ${sentenceEntities.size} sentences")
                }
            }

            Log.d("InitializeDatabase", "Database initialization completed successfully")
        } catch (e: Exception) {
            Log.e("InitializeDatabase", "Error initializing database: ${e.message}")
            createFallbackData()
        }
    }

    private suspend fun createFallbackData() {
        Log.d("InitializeDatabase", "Creating fallback data")

        // Очищаем базу перед созданием fallback данных
        wordRepository.deleteAllWords()
        sentenceRepository.deleteAllSentences()

        // Создаем базовые тестовые данные
        val fallbackWords = listOf(
            WordEntity(
                id = 0, // Автоматическая генерация ID
                norwegian = "hei",
                russian = "привет",
                partOfSpeech = "interjection",
                difficulty = "A1"
            ),
            WordEntity(
                id = 0,
                norwegian = "ja",
                russian = "да",
                partOfSpeech = "particle",
                difficulty = "A1"
            ),
            WordEntity(
                id = 0,
                norwegian = "nei",
                russian = "нет",
                partOfSpeech = "particle",
                difficulty = "A1"
            )
        )

        wordRepository.insertAllWords(fallbackWords)
        Log.d("InitializeDatabase", "Inserted ${fallbackWords.size} fallback words")
    }

    private fun assetExists(context: Context, fileName: String): Boolean {
        return try {
            context.assets.open(fileName).close()
            true
        } catch (e: Exception) {
            false
        }
    }
}