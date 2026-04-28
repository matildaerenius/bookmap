package com.matildaerenius.bookmap.domain.repository

import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.core.Resource

interface BookRepository {
    suspend fun getBooksByIds(bookIds : List<Int>) : Resource<List<Book>>
    suspend fun getBookById(bookId: Int) : Resource<Book>
}