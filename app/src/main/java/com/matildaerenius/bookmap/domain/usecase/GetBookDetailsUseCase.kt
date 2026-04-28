package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.core.Resource
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: Int): Resource<Book> {
        return bookRepository.getBookById(bookId)
    }
}