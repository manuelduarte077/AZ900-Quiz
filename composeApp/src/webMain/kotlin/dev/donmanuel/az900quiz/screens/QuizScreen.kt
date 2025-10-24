package dev.donmanuel.az900quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.donmanuel.az900quiz.QuizViewModel
import dev.donmanuel.az900quiz.composables.AnswerOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBackToStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quizState = viewModel.quizState
    val currentQuestion = viewModel.currentQuestion ?: return

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "AZ-900 Quiz",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToStart) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )

            // Progress Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { viewModel.progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Question Counter and Timer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pregunta ${quizState.currentQuestionIndex + 1} de ${quizState.questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Timer
                            if (!quizState.showResult) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Timer",
                                    tint = when (quizState.examMode) {
                                        dev.donmanuel.az900quiz.data.ExamMode.PRACTICE ->
                                            if (quizState.timeRemaining <= 10)
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer

                                        dev.donmanuel.az900quiz.data.ExamMode.REAL_EXAM ->
                                            if (quizState.totalTimeRemaining <= 300) // 5 minutos
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = viewModel.timeRemainingFormatted,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when (quizState.examMode) {
                                        dev.donmanuel.az900quiz.data.ExamMode.PRACTICE ->
                                            if (quizState.timeRemaining <= 10)
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer

                                        dev.donmanuel.az900quiz.data.ExamMode.REAL_EXAM ->
                                            if (quizState.totalTimeRemaining <= 300) // 5 minutos
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                            }

                            Text(
                                text = "${(viewModel.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Main Content
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 800.dp, min = 320.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Question Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp)
                        ) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            // Answer Options
                            currentQuestion.options.forEach { (key, value) ->
                                AnswerOption(
                                    optionKey = key,
                                    optionText = value,
                                    isSelected = quizState.selectedAnswer == key,
                                    isCorrect = currentQuestion.answer == key,
                                    showResult = quizState.showResult,
                                    onClick = {
                                        if (!quizState.showResult) {
                                            viewModel.selectAnswer(key)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // Immediate Feedback Section
                            if (quizState.showResult) {
                                Spacer(modifier = Modifier.height(20.dp))

                                val lastAnswer = quizState.userAnswers.lastOrNull()
                                val isCorrect = lastAnswer?.isCorrect ?: false
                                val timeSpent = lastAnswer?.timeSpent ?: 0

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isCorrect)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Error,
                                            contentDescription = if (isCorrect) "Correcto" else "Incorrecto",
                                            tint = if (isCorrect)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(32.dp)
                                        )

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = if (isCorrect) "¡Correcto!" else "Incorrecto",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCorrect)
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onErrorContainer
                                            )

                                            Text(
                                                text = "Tiempo utilizado: ${timeSpent}s",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (isCorrect)
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onErrorContainer
                                            )

                                            if (!isCorrect) {
                                                Text(
                                                    text = "Respuesta correcta: ${currentQuestion.answer}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onErrorContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Action Buttons
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (quizState.showResult) {
                        Button(
                            onClick = {
                                if (viewModel.isLastQuestion) {
                                    viewModel.nextQuestion() // This will trigger result screen
                                } else {
                                    viewModel.nextQuestion()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = if (viewModel.isLastQuestion) "Ver Resultados" else "Siguiente",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.submitAnswer() },
                            enabled = quizState.selectedAnswer != null,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Responder",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
