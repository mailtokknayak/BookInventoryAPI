package com.inventory.BookInventory.repository

import com.inventory.BookInventory.model.Book
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BookRepository : ReactiveMongoRepository<Book , String> {

    fun findByTitle(title:String) : Flux<Book>

}