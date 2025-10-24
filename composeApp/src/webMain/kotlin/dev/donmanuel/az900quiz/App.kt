package dev.donmanuel.az900quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.donmanuel.az900quiz.data.QuizScreen
import dev.donmanuel.az900quiz.screens.DetailedSummaryScreen
import dev.donmanuel.az900quiz.screens.ErrorScreen
import dev.donmanuel.az900quiz.screens.LoadingScreen
import dev.donmanuel.az900quiz.screens.StartScreen
import dev.donmanuel.az900quiz.service.QuestionLoader

@Composable
fun App() {
    val viewModel = remember { QuizViewModel() }
    var currentScreen by remember { mutableStateOf(QuizScreen.START) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var allQuestions by remember { mutableStateOf<List<dev.donmanuel.az900quiz.data.Question>>(emptyList()) }

    // Load questions when app starts
    LaunchedEffect(Unit) {
        try {
            val questions = QuestionLoader.loadQuestions(428) // Load all available questions

            if (questions.isNotEmpty()) {
                allQuestions = questions
                viewModel.loadQuestions(questions, 10) // Default to 10 questions
                isLoading = false
            } else {
                loadError = "No se pudieron cargar las preguntas"
                isLoading = false
            }
        } catch (e: Exception) {
            loadError = "Error al cargar las preguntas: ${e.message}"
            isLoading = false
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                isLoading -> {
                    LoadingScreen()
                }

                loadError != null -> {
                    ErrorScreen(
                        error = loadError!!,
                        onRetry = {
                            isLoading = true
                            loadError = null
                        }
                    )
                }

                else -> {
                    when (currentScreen) {
                        QuizScreen.START -> {
                            StartScreen(
                                onStartQuiz = { questionCount, timePerQuestion, examMode ->
                                    viewModel.loadQuestions(allQuestions, questionCount, timePerQuestion, examMode)
                                    viewModel.startQuiz(questionCount, timePerQuestion, examMode)
                                    currentScreen = QuizScreen.QUIZ
                                }
                            )
                        }

                        QuizScreen.QUIZ -> {
                            dev.donmanuel.az900quiz.screens.QuizScreen(
                                viewModel = viewModel,
                                onBackToStart = {
                                    currentScreen = QuizScreen.START
                                }
                            )

                            // Check if quiz is completed
                            LaunchedEffect(viewModel.quizState.quizCompleted) {
                                if (viewModel.quizState.quizCompleted) {
                                    currentScreen = QuizScreen.RESULT
                                }
                            }
                        }

                        QuizScreen.RESULT -> {
                            DetailedSummaryScreen(
                                correctAnswers = viewModel.correctAnswers,
                                incorrectAnswers = viewModel.incorrectAnswers,
                                totalQuestions = viewModel.quizState.questions.size,
                                averageTime = viewModel.averageTimePerQuestion,
                                examScore = viewModel.examScore,
                                passedExam = viewModel.passedExam,
                                onRestartQuiz = {
                                    viewModel.restartQuiz()
                                    currentScreen = QuizScreen.START
                                },
                                onBackToStart = {
                                    currentScreen = QuizScreen.START
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}