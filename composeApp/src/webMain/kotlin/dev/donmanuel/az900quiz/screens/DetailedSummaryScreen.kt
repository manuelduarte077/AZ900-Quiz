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
import dev.donmanuel.az900quiz.composables.AnswerSectionCard
import dev.donmanuel.az900quiz.composables.StatisticItem
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
    val percentage =
        if (totalQuestions > 0) (correctAnswers.size.toFloat() / totalQuestions.toFloat() * 100).toInt() else 0
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
                        Icon(
                            imageVector = if (passed) Icons.Default.Celebration else Icons.Default.Book,
                            contentDescription = if (passed) "Felicitaciones" else "Sigue Practicando",
                            tint = if (passed)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (passed) "¡Felicitaciones!" else "Sigue Practicando",
                            style = MaterialTheme.typography.displaySmall,
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
                            modifier = Modifier.size(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { percentage / 100f },
                                modifier = Modifier.size(160.dp),
                                color = if (passed)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error,
                                strokeWidth = 8.dp,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$examScore/1000",
                                    fontWeight = FontWeight.Bold,
                                    color = if (passed)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = "${correctAnswers.size}/$totalQuestions ($percentage%)",
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