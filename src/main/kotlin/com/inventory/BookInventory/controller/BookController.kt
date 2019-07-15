package com.inventory.BookInventory.controller

import com.inventory.BookInventory.model.Book
import com.inventory.BookInventory.model.GBookData
import com.inventory.BookInventory.repository.BookRepository
import com.inventory.BookInventory.service.BookService
import com.inventory.BookInventory.service.KafkaAuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/v1/book")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class BookController {




    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var kafkaAuditService: KafkaAuditService


    @GetMapping("/allBooks")
    fun getBooks() :Flux<Book>{
        return bookRepository.findAll()
    }

    @PostMapping("/createBook")
    fun createBook(@RequestBody book: Book): Mono<Book> {
        book.id = UUID.randomUUID().toString()
        var response: Mono<Book>  =   bookRepository.save(book)
        response.subscribe(kafkaAuditService :: sendAddMessage)
        return response

    }


    @GetMapping("/findByTitle/{title}")
    fun findByTitle(@PathVariable title:String) : Flux<Book> {
        return  bookRepository.findByTitle(title)
    }


    @DeleteMapping("/deleteBook/{id}")
    fun deleteById(@PathVariable id: String) : Mono<Void>{
        var response: Mono<Book>  =  bookRepository.findById(id)
        var output : Mono<Void> =  bookRepository.deleteById(id)
        response.subscribe(kafkaAuditService :: sendDeleteMessage)
        return output
    }

    @PutMapping("/updateBook/{id}")
    fun updateBookDetails(@PathVariable id: String , @RequestBody book: Book) : Mono<Book>{
        var previousBookData : Mono<Book> = bookRepository.findById(id)
        var response: Mono<Book>  = bookService.updateBookDetails(id, book)
        response.subscribe(kafkaAuditService :: sendEditMessage)
        return response

    }

    @GetMapping("/deleteAll")
    fun deleteAll() {
        bookRepository.deleteAll()
    }




    @GetMapping("/gBooks/{input}")
    fun queryGoogleBooks(@PathVariable input: String): Flux<GBookData> {
        return bookService.fetchGBooks (input)

    }



    @GetMapping("/getBookAudit")
    fun getBookAuditLogs(): List<String>{
        return KafkaAuditService.auditLogs
    }


}