package org.example.project.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.project.book.data.database.DatabaseFactory
import org.example.project.book.data.database.FavoriteBookDatabase
import org.example.project.book.data.network.KtorRemoteBookDataSource
import org.example.project.book.data.network.RemoteBookDataSource
import org.example.project.book.data.repository.DefaultBookRepository
import org.example.project.book.domain.BookRepository
import org.example.project.book.ui.SelectedBookViewModel
import org.example.project.book.ui.book_detail.BookDetailViewModel
import org.example.project.book.ui.book_list.BookListViewModel
import org.example.project.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single {
        get<FavoriteBookDatabase>().favoriteBookDao
    }

    viewModelOf(::BookListViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::BookDetailViewModel)
}