package com.inventory.BookInventory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.CrossOrigin

@SpringBootApplication
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class BookInventoryApplication

fun main(args: Array<String>) {
	runApplication<BookInventoryApplication>(*args)
}
