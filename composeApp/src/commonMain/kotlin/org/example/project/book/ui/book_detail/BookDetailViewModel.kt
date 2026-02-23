package org.example.project.book.ui.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.app.Route
import org.example.project.book.domain.BookRepository
import org.example.project.core.domain.onSuccess

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id
    private var descriptionLoaded = false

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            // Only do this once (until success)
            if (!descriptionLoaded) {
                fetchBookDescription()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookDetailAction) {
        when (action) {
            BookDetailAction.OnBackClick -> Unit
            BookDetailAction.OnFavoriteClick -> {
                // TODO
            }

            is BookDetailAction.OnSelectedBookChange -> {
                if (state.value.book == null) {
                    _state.update {
                        it.copy(book = action.book)
                    }
                }
            }
        }
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    _state.update {
                        it.copy(
                            book = it.book?.copy(description = description),
                            isLoading = false
                        )
                    }.also {
                        descriptionLoaded = true
                    }
                }
        }
    }
}