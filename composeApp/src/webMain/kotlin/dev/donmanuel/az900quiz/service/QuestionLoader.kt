package dev.donmanuel.az900quiz.service

import dev.donmanuel.az900quiz.data.Question
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object QuestionLoader {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val apiUrl = "https://raw.githubusercontent.com/manuelduarte077/AZ900-Quiz/refs/heads/main/api/questions.json"

    suspend fun loadQuestions(count: Int = 10): List<Question> {
        return try {
            val response: HttpResponse = client.get(apiUrl) {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                }
            }

            val jsonText = response.bodyAsText()
            val allQuestions: List<Question> = json.decodeFromString(jsonText)
            allQuestions.shuffled().take(count)
        } catch (e: Exception) {
            println("Error loading questions from API: ${e.message}")
            emptyList()
        }
    }

    suspend fun loadAllQuestions(): List<Question> {
        return try {
            val response: HttpResponse = client.get(apiUrl) {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                }
            }

            val jsonText = response.bodyAsText()
            json.decodeFromString<List<Question>>(jsonText)
        } catch (e: Exception) {
            println("Error loading all questions from API: ${e.message}")
            emptyList()
        }
    }

    suspend fun getQuestionCount(): Int {
        val questions = loadAllQuestions()
        return questions.size
    }
}