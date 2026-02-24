package org.example.project.book.data.repository

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.book.data.database.FavoriteBookDao
import org.example.project.book.data.mappers.toBook
import org.example.project.book.data.mappers.toBookEntity
import org.example.project.book.data.network.RemoteBookDataSource
import org.example.project.book.domain.Book
import org.example.project.book.domain.BookRepository
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.data.map { it.toBook() }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        // Simple caching strategy (get once) without periodic refresh and only for favorite books
        val localResult = favoriteBookDao.get(bookId)
        return if (localResult == null || localResult.description == null) {
            remoteBookDataSource
                .getBookDetails(bookId)
                .map {
                    val description = it.description
                    if (localResult != null && localResult.description == null) {
                        favoriteBookDao.upsert(localResult.copy(description = description))
                    }
                    description
                }
        } else {
            Result.Success(localResult.description)
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavorites()
            .map { entities ->
                entities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavorites()
            .map { entities ->
                entities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (_: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (_: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.delete(id)
    }
}