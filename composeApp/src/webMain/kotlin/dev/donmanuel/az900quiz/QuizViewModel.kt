package dev.donmanuel.az900quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.donmanuel.az900quiz.data.Question
import dev.donmanuel.az900quiz.data.QuizScreen
import dev.donmanuel.az900quiz.data.QuizState

class QuizViewModel {
    var quizState by mutableStateOf(QuizState())
        private set
    
    var currentScreen by mutableStateOf(QuizScreen.START)
        private set
    
    fun loadQuestions(questions: List<Question>) {
        quizState = quizState.copy(
            questions = questions,
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            showResult = false,
            quizCompleted = false
        )
    }
    
    fun startQuiz() {
        currentScreen = QuizScreen.QUIZ
        quizState = quizState.copy(
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            showResult = false,
            quizCompleted = false
        )
    }
    
    fun selectAnswer(answer: String) {
        quizState = quizState.copy(selectedAnswer = answer)
    }
    
    fun submitAnswer() {
        val currentQuestion = quizState.questions[quizState.currentQuestionIndex]
        val isCorrect = quizState.selectedAnswer == currentQuestion.answer
        
        quizState = quizState.copy(
            score = if (isCorrect) quizState.score + 1 else quizState.score,
            showResult = true
        )
    }
    
    fun nextQuestion() {
        val nextIndex = quizState.currentQuestionIndex + 1
        
        if (nextIndex >= quizState.questions.size) {
            // Quiz completed
            quizState = quizState.copy(quizCompleted = true)
            currentScreen = QuizScreen.RESULT
        } else {
            quizState = quizState.copy(
                currentQuestionIndex = nextIndex,
                selectedAnswer = null,
                showResult = false
            )
        }
    }
    
    fun restartQuiz() {
        currentScreen = QuizScreen.START
        quizState = quizState.copy(
            currentQuestionIndex = 0,
            score = 0,
            selectedAnswer = null,
            showResult = false,
            quizCompleted = false
        )
    }
    
    fun goBackToStart() {
        currentScreen = QuizScreen.START
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
}
