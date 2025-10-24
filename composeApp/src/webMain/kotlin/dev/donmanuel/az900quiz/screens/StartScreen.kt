package dev.donmanuel.az900quiz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donmanuel.az900quiz.data.ExamMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    onStartQuiz: (Int, Int, ExamMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val questionCountOptions = listOf(10, 20, 30, 50, 60, 100)
    val timeOptions = listOf(30, 45, 60, 90, 120)
        val examModeOptions = listOf(
            ExamMode.PRACTICE to "Modo Práctica",
            ExamMode.REAL_EXAM to "Examen Real (45 min)"
        )

    var selectedQuestionCount by remember { mutableStateOf(questionCountOptions[0]) }
    var selectedTimePerQuestion by remember { mutableStateOf(timeOptions[2]) }
    var selectedExamMode by remember { mutableStateOf(examModeOptions[0].first) }
    var expandedQuestions by remember { mutableStateOf(false) }
    var expandedTime by remember { mutableStateOf(false) }
    var expandedMode by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 800.dp, min = 320.dp)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Main Quiz Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = "Azure Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = "AZ-900",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Microsoft Azure Fundamentals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Practica para tu examen de certificación con preguntas reales del AZ-900",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "428 preguntas disponibles desde la API",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Question Count Selection
                    Text(
                        text = "Configuración del Quiz:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Questions Count Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedQuestions,
                        onExpandedChange = { expandedQuestions = !expandedQuestions }
                    ) {
                        OutlinedTextField(
                            value = "$selectedQuestionCount preguntas",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Cantidad de preguntas") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedQuestions)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryEditable),
                            shape = RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        DropdownMenu(
                            expanded = expandedQuestions,
                            onDismissRequest = { expandedQuestions = false }
                        ) {
                            questionCountOptions.forEach { count ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "$count preguntas",
                                            fontWeight = if (count == selectedQuestionCount) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedQuestionCount = count
                                        expandedQuestions = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Exam Mode Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedMode,
                        onExpandedChange = { expandedMode = !expandedMode }
                    ) {
                        OutlinedTextField(
                            value = examModeOptions.find { it.first == selectedExamMode }?.second ?: "Modo Práctica",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Modo de Examen") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMode)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryEditable),
                            shape = RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        DropdownMenu(
                            expanded = expandedMode,
                            onDismissRequest = { expandedMode = false }
                        ) {
                            examModeOptions.forEach { (mode, label) ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = label,
                                            fontWeight = if (mode == selectedExamMode) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedExamMode = mode
                                        expandedMode = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Time Per Question Dropdown (solo en modo práctica)
                    if (selectedExamMode == ExamMode.PRACTICE) {
                        ExposedDropdownMenuBox(
                            expanded = expandedTime,
                            onExpandedChange = { expandedTime = !expandedTime }
                        ) {
                            OutlinedTextField(
                                value = "${selectedTimePerQuestion}s por pregunta",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tiempo por pregunta") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable),
                                shape = RoundedCornerShape(16.dp),
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            DropdownMenu(
                                expanded = expandedTime,
                                onDismissRequest = { expandedTime = false }
                            ) {
                                timeOptions.forEach { time ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "${time}s por pregunta",
                                                fontWeight = if (time == selectedTimePerQuestion) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            selectedTimePerQuestion = time
                                            expandedTime = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onStartQuiz(selectedQuestionCount, selectedTimePerQuestion, selectedExamMode) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Comenzar Quiz",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Información del Quiz",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "• 428 preguntas reales del AZ-900\n• Duración oficial: 45 minutos\n• Respuestas inmediatas\n• Progreso en tiempo real\n• Resultados detallados\n• Preguntas aleatorias cada vez",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            lineHeight = 28.sp
                        )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Selected count display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quiz configurado: ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                                text = when (selectedExamMode) {
                                    ExamMode.PRACTICE -> "$selectedQuestionCount preguntas • ${selectedTimePerQuestion}s c/u"
                                    ExamMode.REAL_EXAM -> "$selectedQuestionCount preguntas • 45 minutos totales"
                                },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}