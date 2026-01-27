package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.engine.HttpClientEngine
import org.example.project.book.data.network.KtorRemoteBookDataSource
import org.example.project.book.data.repository.DefaultBookRepository
import org.example.project.book.ui.book_list.BookListScreen
import org.example.project.book.ui.book_list.BookListViewModel
import org.example.project.core.data.HttpClientFactory

@Composable
@Preview
fun App(engine: HttpClientEngine) {
    MaterialTheme {
        BookListScreen(
            viewModel = remember {
                BookListViewModel(
                    bookRepository = DefaultBookRepository(
                        remoteBookDataSource = KtorRemoteBookDataSource(
                            httpClient = HttpClientFactory.create(
                                engine = engine
                            )
                        )
                    )
                )
            },
            onBookClick = {}
        )
    }
}