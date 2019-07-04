package com.inventory.BookInventory.model

data class GBookData(val items: List<BookData>)

data class BookData(val volumeInfo: Book?)