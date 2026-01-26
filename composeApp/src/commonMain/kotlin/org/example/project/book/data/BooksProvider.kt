package org.example.project.book.data

import org.example.project.book.domain.Book
import kotlin.random.Random

object BooksProvider {
    val data = (1..50).map {
        Book(
            id = "$it",
            title = "Title $it",
            imageUrl = "",
            authors = listOf("Author $it"),
            description = "Description $it",
            languages = listOf("English"),
            firstPublishYear = "2010",
            averageRating = Random.nextDouble(3.0, 5.0),
            ratingCount = 3,
            numPages = 10,
            numEditions = 1
        )
    }
}