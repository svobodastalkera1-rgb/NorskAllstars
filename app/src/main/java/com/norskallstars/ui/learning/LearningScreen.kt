package com.norskallstars.ui.learning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.norskallstars.R
import kotlinx.coroutines.delay
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LearningScreen(
    viewModel: LearningViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val recordAudioPermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    LaunchedEffect(recordAudioPermissionState.status) {
        if (recordAudioPermissionState.status != PermissionStatus.Granted) {
            recordAudioPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Обучение") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is LearningScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LearningScreenState.Question -> {
                    QuestionContent(
                        question = currentState,
                        hasPermission = recordAudioPermissionState.status == PermissionStatus.Granted,
                        onSpeak = viewModel::speakText,
                        onAnswer = { answer ->
                            val result = viewModel.checkAnswer(answer)
                            if (result.isCorrect) {
                                viewModel.playSuccessSound()
                                viewModel.nextQuestion()
                            } else {
                                viewModel.playErrorSound()
                                viewModel.showResult(result)
                            }
                        }
                    )
                }
                is LearningScreenState.Result -> {
                    // Создаем объект Question из данных Result
                    val questionFromResult = LearningScreenState.Question(
                        word = currentState.word,
                        options = currentState.options,
                        correctAnswer = currentState.correctAnswer,
                        sentence = currentState.sentence
                    )

                    QuestionContent(
                        question = questionFromResult,
                        hasPermission = recordAudioPermissionState.status == PermissionStatus.Granted,
                        onSpeak = viewModel::speakText,
                        onAnswer = { },
                        selectedAnswer = currentState.selectedAnswer,
                        showResult = true
                    )

                    // Всплывающее окно с результатом только для неправильных ответов
                    if (!currentState.isCorrect) {
                        ResultBottomSheet(
                            result = currentState,
                            onDismiss = { viewModel.nextQuestion() },
                            onSpeak = viewModel::speakText
                        )
                    }
                }
                is LearningScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentState.message,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(onClick = { onBack() }) {
                                Text(text = "Вернуться")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionContent(
    question: LearningScreenState.Question,
    hasPermission: Boolean,
    onSpeak: (String) -> Unit,
    onAnswer: (String) -> Unit,
    selectedAnswer: String? = null,
    showResult: Boolean = false
) {
    val animationDuration = 400

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Анимированное появление вопроса
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(animationDuration)) +
                    slideInHorizontally(tween(animationDuration)) { it },
            exit = fadeOut(tween(animationDuration)) +
                    slideOutHorizontally(tween(animationDuration)) { -it }
        ) {
            Text(
                text = "Переведите слово:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Анимированная карточка слова
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(animationDuration, 100)) +
                    slideInHorizontally(tween(animationDuration, 100)) { it },
            exit = fadeOut(tween(animationDuration)) +
                    slideOutHorizontally(tween(animationDuration)) { -it }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.word.norwegian,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .clickable {
                                if (hasPermission) {
                                    onSpeak(question.word.norwegian)
                                }
                            }
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Озвучить",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Озвучить слово")
                    }
                }
            }
        }

        question.sentence?.let { sentence ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(animationDuration, 200)) +
                        slideInHorizontally(tween(animationDuration, 200)) { it },
                exit = fadeOut(tween(animationDuration)) +
                        slideOutHorizontally(tween(animationDuration)) { -it }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Пример предложения:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = sentence,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .clickable {
                                    if (hasPermission) {
                                        onSpeak(sentence)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Озвучить предложение",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Озвучить предложение",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Анимированные варианты ответов с подсветкой
        question.options.forEachIndexed { index, option ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(animationDuration, 300 + index * 100)) +
                        slideInHorizontally(tween(animationDuration, 300 + index * 100)) { it },
                exit = fadeOut(tween(animationDuration)) +
                        slideOutHorizontally(tween(animationDuration)) { -it }
            ) {
                // Определяем цвет кнопки в зависимости от результата
                val backgroundColor = when {
                    !showResult -> MaterialTheme.colorScheme.primary
                    option == question.correctAnswer -> Color(0xFF4CAF50) // Зеленый для правильного
                    option == selectedAnswer -> Color(0xFFF44336) // Красный для неправильного выбора
                    else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                }

                Button(
                    onClick = {
                        if (!showResult) {
                            onAnswer(option)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                        contentColor = Color.White
                    ),
                    enabled = !showResult
                ) {
                    Text(text = option, fontSize = 18.sp)
                }
            }
        }

        if (!hasPermission) {
            Text(
                text = "Для озвучки требуется разрешение на запись аудио",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ResultBottomSheet(
    result: LearningScreenState.Result,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit
) {
    var offsetY by remember { mutableStateOf(0f) }
    var isVisible by remember { mutableStateOf(true) }
    var showNextButton by remember { mutableStateOf(false) }

    if (!isVisible) {
        // Показываем кнопку "ДАЛЕЕ" после скрытия окна
        if (showNextButton) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        showNextButton = false
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "ДАЛЕЕ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    // Затемнение фона
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .zIndex(1f)
            .clickable { /* Блокируем клики на фоне */ }
    )

    // Всплывающее окно
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = offsetY.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        // Разрешаем свайп в обе стороны, но ограничиваем сверху
                        if (dragAmount > 0 || offsetY + dragAmount < 0) {
                            offsetY += dragAmount

                            // Если свайпнули достаточно далеко вниз - скрываем окно
                            if (offsetY > 300) {
                                isVisible = false
                                showNextButton = true
                            }

                            // Ограничиваем свайп вверх (не выше исходного положения)
                            if (offsetY < 0) {
                                offsetY = 0f
                            }
                        }
                    }
                }
        ) {
            // Полоска для свайпа
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(4.dp)
                        .background(Color.Gray.copy(alpha = 0.5f))
                        .clip(RoundedCornerShape(2.dp))
                        .align(Alignment.Center)
                )
            }

            // Контент результата с увеличенным текстом
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Неправильно 😕",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFF44336),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Правильный ответ:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "${result.word.norwegian} - ${result.word.russian}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                result.sentence?.let { sentence ->
                    Text(
                        text = "Пример: $sentence",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Кнопка "ДАЛЕЕ"
                Button(
                    onClick = {
                        isVisible = false
                        showNextButton = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "ДАЛЕЕ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}