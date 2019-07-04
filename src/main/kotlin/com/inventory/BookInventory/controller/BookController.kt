package com.inventory.BookInventory.controller

import com.inventory.BookInventory.model.Book
import com.inventory.BookInventory.model.GBookData
import com.inventory.BookInventory.repository.BookRepository
import com.inventory.BookInventory.service.BookService
import com.inventory.BookInventory.service.GBookApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class BookController {




    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var bookService: BookService


    @GetMapping("/allBooks")
    fun getBooks() :Flux<Book>{
       return bookRepository.findAll()
    }

    @PostMapping("/createBook")
    fun createBook(@RequestBody book: Book) = bookRepository.save(book)

    @GetMapping("/findByTitle/{title}")
    fun findByTitle(@PathVariable title:String) : Flux<Book> {
        return  bookRepository.findByTitle(title)
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @DeleteMapping("/deleteBook/{id}")
    fun deleteById(@PathVariable id: String) : Mono<Void>{
       return bookRepository.deleteById(id)
    }

    @PutMapping("/updateBook/{id}")
    fun updateBookDetails(@PathVariable id: String , @RequestBody book: Book) : Mono<Book>{
       return bookService.updateBookDetails(id, book)

    }

    @GetMapping("/deleteAll")
    fun deleteAll() {
         bookRepository.deleteAll()
    }



    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @GetMapping("/gBooks/{input}")
    fun queryGoogleBooks(@PathVariable input: String): Flux<GBookData> {
        return bookService.fetchGBooks (input)

    }

//    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
//    @GetMapping("/gBooks/{input}")
//    fun queryGoogleBooks1(@PathVariable input: String): Flux<GBookData> {
//        return  gBookApiService.gBookApi (input)
//
//    }

}