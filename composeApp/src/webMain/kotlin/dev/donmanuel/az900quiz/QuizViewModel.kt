package dev.donmanuel.az900quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.donmanuel.az900quiz.data.Question
import dev.donmanuel.az900quiz.data.QuizScreen
import dev.donmanuel.az900quiz.data.QuizState
import dev.donmanuel.az900quiz.data.UserAnswer
import dev.donmanuel.az900quiz.data.ExamMode
import dev.donmanuel.az900quiz.data.QuestionType
import kotlin.js.js

class QuizViewModel {
    var quizState by mutableStateOf(QuizState())
        private set
    var currentScreen by mutableStateOf(QuizScreen.START)
        private set

    private var timerJob: dynamic = null

    fun loadQuestions(
        questions: List<Question>,
        questionCount: Int = 10,
        timePerQuestion: Int = 60,
        examMode: ExamMode = ExamMode.PRACTICE
    ) {
        val limitedQuestions = questions.shuffled().take(questionCount)
        val totalTime =
            if (examMode == ExamMode.REAL_EXAM) 90 * 60 else questionCount * timePerQuestion // 90 minutos en segundos

        quizState = quizState.copy(
            questions = limitedQuestions,
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            selectedAnswers = emptyList(),
            showResult = false,
            quizCompleted = false,
            selectedQuestionCount = questionCount,
            timePerQuestion = timePerQuestion,
            timeRemaining = if (examMode == ExamMode.PRACTICE) timePerQuestion else 0,
            totalTimeRemaining = totalTime,
            questionStartTime = getCurrentTime(),
            userAnswers = emptyList(),
            examMode = examMode
        )
    }

    fun startQuiz(questionCount: Int = 10, timePerQuestion: Int = 60, examMode: ExamMode = ExamMode.PRACTICE) {
        currentScreen = QuizScreen.QUIZ
        val totalTime = if (examMode == ExamMode.REAL_EXAM) 90 * 60 else questionCount * timePerQuestion

        quizState = quizState.copy(
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            selectedAnswers = emptyList(),
            showResult = false,
            quizCompleted = false,
            selectedQuestionCount = questionCount,
            timePerQuestion = timePerQuestion,
            timeRemaining = if (examMode == ExamMode.PRACTICE) timePerQuestion else 0,
            totalTimeRemaining = totalTime,
            questionStartTime = getCurrentTime(),
            userAnswers = emptyList(),
            examMode = examMode
        )
        startTimer()
    }

    fun selectAnswer(answer: String) {
        if (quizState.examMode == ExamMode.PRACTICE) {
            quizState = quizState.copy(selectedAnswer = answer)
        } else {
            // En modo examen real, solo guardamos la respuesta sin mostrar resultado inmediato
            quizState = quizState.copy(selectedAnswer = answer)
        }
    }

    fun selectMultipleAnswers(answers: List<String>) {
        quizState = quizState.copy(selectedAnswers = answers)
    }

    fun submitAnswer() {
        val currentQuestion = quizState.questions[quizState.currentQuestionIndex]
        val timeSpent = if (quizState.examMode == ExamMode.PRACTICE) {
            quizState.timePerQuestion - quizState.timeRemaining
        } else {
            // En modo examen real, calculamos tiempo desde el inicio de la pregunta
            ((getCurrentTime() - quizState.questionStartTime) / 1000).toInt()
        }

        val isCorrect = when (currentQuestion.questionType) {
            QuestionType.SINGLE_CHOICE -> quizState.selectedAnswer == currentQuestion.answer
            QuestionType.MULTIPLE_CHOICE -> {
                val correctAnswers = currentQuestion.answer.split(",").map { it.trim() }
                quizState.selectedAnswers.sorted() == correctAnswers.sorted()
            }

            QuestionType.TRUE_FALSE -> quizState.selectedAnswer == currentQuestion.answer
            else -> quizState.selectedAnswer == currentQuestion.answer
        }

        val userAnswer = UserAnswer(
            questionId = currentQuestion.id,
            selectedAnswer = quizState.selectedAnswer,
            selectedAnswers = quizState.selectedAnswers,
            correctAnswer = currentQuestion.answer,
            correctAnswers = if (currentQuestion.questionType == QuestionType.MULTIPLE_CHOICE) {
                currentQuestion.answer.split(",").map { it.trim() }
            } else emptyList(),
            isCorrect = isCorrect,
            timeSpent = timeSpent,
            questionText = currentQuestion.question,
            questionType = currentQuestion.questionType,
            domain = currentQuestion.domain
        )

        quizState = quizState.copy(
            score = if (isCorrect) quizState.score + 1 else quizState.score,
            showResult = quizState.examMode == ExamMode.PRACTICE, // Solo mostrar resultado en modo práctica
            userAnswers = quizState.userAnswers + userAnswer
        )

        if (quizState.examMode == ExamMode.PRACTICE) {
            stopTimer()
        }
    }

    fun nextQuestion() {
        val nextIndex = quizState.currentQuestionIndex + 1

        if (nextIndex >= quizState.questions.size) {
            quizState = quizState.copy(quizCompleted = true)
            currentScreen = QuizScreen.RESULT
        } else {
            quizState = quizState.copy(
                currentQuestionIndex = nextIndex,
                selectedAnswer = null,
                selectedAnswers = emptyList(),
                showResult = false,
                timeRemaining = if (quizState.examMode == ExamMode.PRACTICE) quizState.timePerQuestion else 0,
                questionStartTime = getCurrentTime()
            )
            if (quizState.examMode == ExamMode.PRACTICE) {
                startTimer()
            }
        }
    }

    fun restartQuiz() {
        stopTimer()
        currentScreen = QuizScreen.START
        quizState = quizState.copy(
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            showResult = false,
            quizCompleted = false,
            timeRemaining = quizState.timePerQuestion,
            questionStartTime = getCurrentTime(),
            userAnswers = emptyList()
        )
    }

    fun goBackToStart() {
        stopTimer()
        currentScreen = QuizScreen.START
    }

    private fun startTimer() {
        stopTimer()
        timerJob = js("setInterval").unsafeCast<(Any, Int) -> dynamic>().invoke({
            when (quizState.examMode) {
                ExamMode.PRACTICE -> {
                    if (quizState.timeRemaining > 0 && !quizState.showResult) {
                        quizState = quizState.copy(timeRemaining = quizState.timeRemaining - 1)
                    } else if (quizState.timeRemaining <= 0 && !quizState.showResult) {
                        submitAnswer()
                    }
                }

                ExamMode.REAL_EXAM -> {
                    if (quizState.totalTimeRemaining > 0) {
                        quizState = quizState.copy(totalTimeRemaining = quizState.totalTimeRemaining - 1)
                    } else {
                        quizState = quizState.copy(quizCompleted = true)
                        currentScreen = QuizScreen.RESULT
                        stopTimer()
                    }
                }
            }
        }, 1000)
    }

    private fun stopTimer() {
        if (timerJob != null) {
            js("clearInterval").unsafeCast<(dynamic) -> Unit>().invoke(timerJob)
            timerJob = null
        }
    }

    private fun getCurrentTime(): Long {
        return js("Date.now").unsafeCast<() -> Double>().invoke().toLong()
    }

    val currentQuestion: Question?
        get() = if (quizState.currentQuestionIndex < quizState.questions.size) {
            quizState.questions[quizState.currentQuestionIndex]
        } else null

    val progress: Float
        get() = if (quizState.questions.isNotEmpty()) {
            (quizState.currentQuestionIndex + 1).toFloat() / quizState.questions.size.toFloat()
        } else 0f

    val isLastQuestion: Boolean
        get() = quizState.currentQuestionIndex >= quizState.questions.size - 1

    val correctAnswers: List<UserAnswer>
        get() = quizState.userAnswers.filter { it.isCorrect }

    val incorrectAnswers: List<UserAnswer>
        get() = quizState.userAnswers.filter { !it.isCorrect }

    val averageTimePerQuestion: Float
        get() = if (quizState.userAnswers.isNotEmpty()) {
            quizState.userAnswers.map { it.timeSpent }.average().toFloat()
        } else 0f

    val examScore: Int
        get() = if (quizState.questions.isNotEmpty()) {
            val percentage = (quizState.score.toFloat() / quizState.questions.size.toFloat() * 100).toInt()
            when {
                percentage >= 100 -> 1000
                percentage >= 90 -> 900 + (percentage - 90) * 10
                percentage >= 80 -> 800 + (percentage - 80) * 10
                percentage >= 70 -> 700 + (percentage - 70) * 10
                percentage >= 60 -> 600 + (percentage - 60) * 10
                percentage >= 50 -> 500 + (percentage - 50) * 10
                else -> percentage * 10
            }
        } else 0

    val passedExam: Boolean
        get() = examScore >= 700

    val timeRemainingFormatted: String
        get() = when (quizState.examMode) {
            ExamMode.PRACTICE -> "${quizState.timeRemaining}s"
            ExamMode.REAL_EXAM -> {
                val minutes = quizState.totalTimeRemaining / 60
                val seconds = quizState.totalTimeRemaining % 60
                "${minutes}:${seconds.toString().padStart(2, '0')}"
            }
        }
}
