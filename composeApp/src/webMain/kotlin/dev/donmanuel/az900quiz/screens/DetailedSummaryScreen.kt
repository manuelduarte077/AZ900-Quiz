package dev.donmanuel.az900quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donmanuel.az900quiz.data.UserAnswer

@Composable
fun DetailedSummaryScreen(
    correctAnswers: List<UserAnswer>,
    incorrectAnswers: List<UserAnswer>,
    totalQuestions: Int,
    averageTime: Float,
    examScore: Int,
    passedExam: Boolean,
    onRestartQuiz: () -> Unit,
    onBackToStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalQuestions > 0) (correctAnswers.size.toFloat() / totalQuestions.toFloat() * 100).toInt() else 0
    val passed = passedExam

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 800.dp, min = 320.dp)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (passed)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Status Icon
                        Box(
                            modifier = Modifier.size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (passed) "🎉" else "📚",
                                style = MaterialTheme.typography.displayLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (passed) "¡Felicitaciones!" else "Sigue Practicando",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (passed)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (passed) "Has aprobado el quiz" else "Necesitas más práctica",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (passed)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Score Circle
                        Box(
                            modifier = Modifier.size(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { percentage / 100f },
                                modifier = Modifier.size(140.dp),
                                color = if (passed)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error,
                                strokeWidth = 12.dp,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                            )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$examScore/1000",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (passed)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "${correctAnswers.size}/$totalQuestions ($percentage%)",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (passed)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Statistics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatisticItem(
                                icon = Icons.Default.Timer,
                                label = "Tiempo Promedio",
                                value = "${averageTime.toInt()}s",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            
                            StatisticItem(
                                icon = Icons.Default.CheckCircle,
                                label = "Correctas",
                                value = "${correctAnswers.size}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            
                            StatisticItem(
                                icon = Icons.Default.Error,
                                label = "Incorrectas",
                                value = "${incorrectAnswers.size}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Correct Answers Section
            if (correctAnswers.isNotEmpty()) {
                item {
                    AnswerSectionCard(
                        title = "Respuestas Correctas",
                        answers = correctAnswers,
                        isCorrect = true,
                        icon = Icons.Default.CheckCircle
                    )
                }
            }

            // Incorrect Answers Section
            if (incorrectAnswers.isNotEmpty()) {
                item {
                    AnswerSectionCard(
                        title = "Respuestas Incorrectas",
                        answers = incorrectAnswers,
                        isCorrect = false,
                        icon = Icons.Default.Error
                    )
                }
            }

            // Action Buttons
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onRestartQuiz,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Intentar de Nuevo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onBackToStart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Volver al Inicio",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
private fun AnswerSectionCard(
    title: String,
    answers: List<UserAnswer>,
    isCorrect: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isCorrect) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${answers.size} preguntas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCorrect) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            answers.forEach { answer ->
                AnswerDetailItem(answer = answer, isCorrect = isCorrect)
                if (answer != answers.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun AnswerDetailItem(
    answer: UserAnswer,
    isCorrect: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = answer.questionText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tu respuesta: ${answer.selectedAnswer ?: "Sin respuesta"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!isCorrect) {
                        Text(
                            text = "Correcta: ${answer.correctAnswer}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Text(
                    text = "${answer.timeSpent}s",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
