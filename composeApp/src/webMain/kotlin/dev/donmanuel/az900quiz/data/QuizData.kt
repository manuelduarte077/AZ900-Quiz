package dev.donmanuel.az900quiz.data

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val question: String,
    val options: Map<String, String>,
    val answer: String,
    val questionType: QuestionType = QuestionType.SINGLE_CHOICE,
    val domain: ExamDomain = ExamDomain.CLOUD_CONCEPTS,
    val explanation: String? = null
)

@Serializable
enum class QuestionType {
    SINGLE_CHOICE,      
    MULTIPLE_CHOICE,    
    TRUE_FALSE,         
    MATCHING,           
    DRAG_DROP,          
    CASE_STUDY          
}

@Serializable
enum class ExamDomain(val weight: Int, val description: String) {
    CLOUD_CONCEPTS(25, "Conceptos de la nube"),
    AZURE_SERVICES(20, "Servicios principales de Azure"),
    MANAGEMENT_TOOLS(15, "Soluciones y herramientas de gestión"),
    SECURITY_NETWORKING(15, "Seguridad y características de red"),
    IDENTITY_GOVERNANCE(25, "Identidad, gobernanza, privacidad y cumplimiento"),
    COST_SLA(10, "Gestión de costes y SLA")
}

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val selectedAnswers: List<String> = emptyList(),
    val showResult: Boolean = false,
    val quizCompleted: Boolean = false,
    val timeRemaining: Int = 0,
    val selectedQuestionCount: Int = 10,
    val timePerQuestion: Int = 60, 
    val totalTimeRemaining: Int = 0,
    val questionStartTime: Long = 0L,
    val userAnswers: List<UserAnswer> = emptyList(),
    val examMode: ExamMode = ExamMode.PRACTICE
)

enum class ExamMode {
    PRACTICE, 
    REAL_EXAM
}

data class UserAnswer(
    val questionId: Int,
    val selectedAnswer: String?,
    val selectedAnswers: List<String> = emptyList(),
    val correctAnswer: String,
    val correctAnswers: List<String> = emptyList(),
    val isCorrect: Boolean,
    val timeSpent: Int,
    val questionText: String,
    val questionType: QuestionType,
    val domain: ExamDomain
)

enum class QuizScreen {
    START,
    QUIZ,
    RESULT
}
