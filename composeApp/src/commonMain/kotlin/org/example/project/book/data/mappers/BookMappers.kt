package org.example.project.book.data.mappers

import org.example.project.book.data.dto.BookDto
import org.example.project.book.domain.Book

fun BookDto.toBook(): Book {
    val imageUrl = if (coverKey != null) {
        "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
    } else {
        "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
    }
    return Book(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
    )
}