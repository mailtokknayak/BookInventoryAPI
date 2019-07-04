package com.inventory.BookInventory.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.annotation.processing.Generated


@Document
data class Book(@Id   var id: String?,
                var title: String?,
                var authors: List<String>?,
                var price: Long?,
                var description : String?,
                var quantity: Int?,
                var imageLinks: ImageUrl?)

data class ImageUrl(val smallThumbnail: String?, val thumbnail: String?)