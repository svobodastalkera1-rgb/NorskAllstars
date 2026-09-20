package com.norskallstars.ui.learning

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norskallstars.data.local.entities.WordEntity
import com.norskallstars.domain.use_case.GetRandomWords
import com.norskallstars.domain.use_case.GetSentencesForWord
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import com.norskallstars.R

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val getRandomWords: GetRandomWords,
    private val getSentencesForWord: GetSentencesForWord,
    @ApplicationContext private val context: Context
) : ViewModel(), TextToSpeech.OnInitListener {

    private val _state = MutableStateFlow<LearningScreenState>(LearningScreenState.Loading)
    val state: StateFlow<LearningScreenState> = _state

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var successSoundPlayer: MediaPlayer? = null
    private var errorSoundPlayer: MediaPlayer? = null
    private var currentQuestion: LearningScreenState.Question? = null

    init {
        tts = TextToSpeech(context, this)
        initializeSoundPlayers()
    }

    private fun initializeSoundPlayers() {
        try {
            // Инициализируем плеер для успешного ответа
            successSoundPlayer = MediaPlayer.create(context, R.raw.success_sound)

            // Для ошибок используем тот же файл, но с измененными параметрами
            errorSoundPlayer = MediaPlayer.create(context, R.raw.error_sound).apply {
                setVolume(0.7f, 0.7f) // Немного тише
                setPlaybackParams(playbackParams.apply {
                    pitch = 0.7f // Более низкий тон для ошибок
                })
            }
        } catch (e: Exception) {
            // Логируем ошибку, но не прерываем работу приложения
            android.util.Log.e("LearningViewModel", "Error initializing sound players: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("no", "NO"))
            isTtsInitialized = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
            loadNewQuestion()
        }
    }

    private fun loadNewQuestion() {
        viewModelScope.launch {
            try {
                val words = getRandomWords(10)
                if (words.size < 6) {
                    _state.value = LearningScreenState.Error("Недостаточно слов в базе данных")
                    return@launch
                }

                val correctWord = words.random()
                val otherWords = words.filter { it.id != correctWord.id }.shuffled().take(5)
                val options = (listOf(correctWord.russian) + otherWords.map { it.russian }).shuffled()

                val sentences = getSentencesForWord(correctWord.id).first()
                val sentence = sentences.randomOrNull()?.norwegian

                currentQuestion = LearningScreenState.Question(
                    word = correctWord,
                    options = options,
                    correctAnswer = correctWord.russian,
                    sentence = sentence
                )

                _state.value = currentQuestion!!

                if (isTtsInitialized) {
                    tts?.speak(correctWord.norwegian, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } catch (e: Exception) {
                _state.value = LearningScreenState.Error("Ошибка загрузки вопроса: ${e.message}")
            }
        }
    }

    fun checkAnswer(selectedAnswer: String): LearningScreenState.Result {
        val currentState = _state.value
        return if (currentState is LearningScreenState.Question) {
            val isCorrect = selectedAnswer == currentState.correctAnswer
            LearningScreenState.Result(
                isCorrect = isCorrect,
                selectedAnswer = selectedAnswer,
                word = currentState.word,
                options = currentState.options,
                correctAnswer = currentState.correctAnswer,
                sentence = currentState.sentence
            )
        } else {
            throw IllegalStateException("checkAnswer called in invalid state: $currentState")
        }
    }

    fun playSuccessSound() {
        try {
            successSoundPlayer?.let {
                if (it.isPlaying) {
                    it.seekTo(0)
                }
                it.start()
            }
        } catch (e: Exception) {
            android.util.Log.e("LearningViewModel", "Error playing success sound: ${e.message}")
        }
    }

    fun playErrorSound() {
        try {
            errorSoundPlayer?.let {
                if (it.isPlaying) {
                    it.seekTo(0)
                }
                it.start()
            }
        } catch (e: Exception) {
            android.util.Log.e("LearningViewModel", "Error playing error sound: ${e.message}")
        }
    }

    fun showResult(result: LearningScreenState.Result) {
        _state.value = result
    }

    fun nextQuestion() {
        loadNewQuestion()
    }

    fun speakText(text: String) {
        if (isTtsInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        successSoundPlayer?.release()
        errorSoundPlayer?.release()
        successSoundPlayer = null
        errorSoundPlayer = null
    }
}