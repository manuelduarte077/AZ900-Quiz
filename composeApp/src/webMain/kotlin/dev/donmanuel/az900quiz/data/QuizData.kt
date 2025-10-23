package dev.donmanuel.az900quiz.data

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val question: String,
    val options: Map<String, String>,
    val answer: String
)

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val showResult: Boolean = false,
    val quizCompleted: Boolean = false,
    val timeRemaining: Int = 0,
    val selectedQuestionCount: Int = 10
)

enum class QuizScreen {
    START,
    QUIZ,
    RESULT
}
