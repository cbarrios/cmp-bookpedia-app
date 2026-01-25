package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.book.ui.book_list.BookListScreen
import org.example.project.book.ui.book_list.BookListViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        BookListScreen(
            viewModel = remember { BookListViewModel() },
            onBookClick = {}
        )
    }
}