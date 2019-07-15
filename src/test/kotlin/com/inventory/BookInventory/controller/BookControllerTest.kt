package com.inventory.BookInventory.controller

import com.inventory.BookInventory.model.Book
import com.inventory.BookInventory.repository.BookRepository
import com.inventory.BookInventory.service.BookService
import com.inventory.BookInventory.service.GBookApiService
import com.inventory.BookInventory.service.KafkaAuditService
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import reactor.test.StepVerifier


@RunWith(MockitoJUnitRunner::class)
class BookControllerTest {


    @InjectMocks
    lateinit var booksController: BookController

    @Mock
    lateinit var booksRepository: BookRepository

    @Mock
    lateinit var bookService: BookService

    @Mock
    lateinit var kafkaAuditService: KafkaAuditService

    @Mock
    lateinit var gBookApiService: GBookApiService

    var bookList: ArrayList<Book> = ArrayList()


    @Before
    fun setUp() {
        bookList.add(Book("1", "Unit Test", listOf("Unit Test"), 100, "Unit Test", 10, null))


    }

    @After
    fun tearDown() {
    }

    @Test
    fun shouldCreateNewBookInBookRepository() {

        doReturn(bookList[0].toMono()).`when`(booksRepository).save(bookList[0])
        var result = booksController.createBook(bookList[0])
        verify(booksRepository, times(1)).save(bookList[0])
        StepVerifier
                .create<Any>(result)
                .assertNext { book ->
                    assertNotNull(book)

                }
                .expectComplete()
                .verify()

        assertNotNull(result)


    }

    @Test
    fun shouldFetchAllBooksFromBookRepository() {

        doReturn(bookList.toFlux()).`when`(booksRepository).findAll()
        var result = booksController.getBooks()
        verify(booksRepository, times(1)).findAll()
        assertNotNull(result)

        StepVerifier
                .create<Any>(result)
                .assertNext { book ->
                    assertNotNull(book)
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun shouldFetchBookByTitle() {

        doReturn(bookList.toFlux()).`when`(booksRepository).findByTitle("Unit Test")
        var result = booksController.findByTitle("Unit Test")
        verify(booksRepository, times(1)).findByTitle("Unit Test")
        assertNotNull(result)

        StepVerifier
                .create<Any>(result)
                .assertNext { book ->
                    assertNotNull(book)
//                    assertEquals(bookList[0].id, book.)
                }
                .expectComplete()
                .verify()
    }

//    @Test
//    fun shouldUpdateBookInBooksRepositoryById() {
//        var updatedBook: Book = Book("1", "Unit Test", listOf("Unit Test"), 100, "Unit Test", 10, null)
//        doReturn(updatedBook.toMono()).`when`(bookService).updateBookDetails("1", updatedBook)
//        doReturn(updatedBook.toMono()).`when`(booksRepository).save(updatedBook)
//        var result = booksController.updateBookDetails("1", updatedBook)
//        assertNotNull(result)
//
//        StepVerifier
//                .create<Any>(result)
//                .assertNext { book ->
//                    assertNotNull(book)
//                }
//                .expectComplete()
//                .verify()
//    }

//    @Test
//    fun shouldDeleteBookInBooksRepositoryById() {
//
//    var id: String? = bookList[0].id
//        var deleteResult: Mono<Void> = Mono.empty()
//
//        doReturn(deleteResult).`when`(booksRepository).deleteById(id)
//
//        var result = booksController.deleteById(id)
//        verify(booksRepository, times(1)).deleteById(id)
//        assertNotNull(result)
//
//    }

    @Test
    fun shouldFetchBooksFromGoogleBookAPI() {
        doReturn(bookList.toFlux()).`when`(bookService).fetchGBooks("CHANDRAKANTA")
//        doReturn(bookList.toFlux()).`when`(gBookApiService).gBookApi("CHANDRAKANTA")
        var result = booksController.queryGoogleBooks("CHANDRAKANTA")
        verify(bookService, times(1)).fetchGBooks("CHANDRAKANTA")
        assertNotNull(result)
    }
}