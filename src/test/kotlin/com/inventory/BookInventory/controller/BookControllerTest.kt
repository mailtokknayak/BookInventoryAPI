package com.inventory.BookInventory.controller


import com.inventory.BookInventory.model.Book
import com.inventory.BookInventory.model.ImageUrl
import com.inventory.BookInventory.repository.BookRepository
import com.inventory.BookInventory.service.BookService
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class BookControllerTest {






    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var booksRepository:BookRepository





    var books: ArrayList<Book> = ArrayList()

    lateinit var img : ImageUrl

    @Before
    fun setUp() {

        books.add(Book("100","Book1", listOf("Author1"),250,"Desc",1, null))
        books.add(Book("101","Book2", listOf("Author2"),300,"Desc",2, null))
        booksRepository.saveAll(
                books).then().block()
    }

    @After
    fun tearDown() {
        booksRepository.deleteAll()

    }


    @Test
    fun shouldReturnAllBookListFromDatabase() {

        webTestClient.get().uri("/v1/book/allBooks").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Book::class.java)
                .contains(books[0])
    }


    @Test
    fun shouldCreateBookWithBookObject(){

        var book: Book = Book("102","Test", listOf("test"),999,"test",1,null)

        var response =  webTestClient.post().uri("/v1/book/createBook")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(book))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book::class.java)
                .returnResult()

    }

    @Test
    fun shouldFindByTitle() {

        webTestClient.get().uri("/v1/book/findByTitle/{title}", "Book1" ).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Book::class.java)
                .contains(books[0])
    }

    @Test
    fun shouldDeleteBookById() {

        webTestClient.delete().uri("/v1/book/deleteBook/{id}", 100)
                .exchange()
                .expectStatus().isOk
    }



    @Test
    fun shouldUpdateBookDetails(){

        var book: Book = Book("103","Test", listOf("test"),999,"test",1,null)

        var response =  webTestClient.put().uri("/v1/book//updateBook/{id}", "001")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(book))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book::class.java)
                .returnResult()

    }




}
