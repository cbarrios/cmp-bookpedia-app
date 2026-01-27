package org.example.project.book.data.network

import org.example.project.book.data.dto.SearchResponseDto
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>
}