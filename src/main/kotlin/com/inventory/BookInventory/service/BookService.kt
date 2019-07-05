package com.inventory.BookInventory.service

import com.inventory.BookInventory.model.Book
import com.inventory.BookInventory.model.GBookData
import com.inventory.BookInventory.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BookService{

    @Autowired
    lateinit var gBookApiService: GBookApiService

    @Autowired
    lateinit var bookRepository: BookRepository

    fun fetchGBooks(input: String): Flux<GBookData> {
      return gBookApiService.gBookApi(input)
    }

    fun updateBookDetails(id:String , book: Book) : Mono<Book> {
       return bookRepository.save(book)
    }
}