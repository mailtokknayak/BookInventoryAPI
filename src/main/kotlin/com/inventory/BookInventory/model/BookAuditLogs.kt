package com.inventory.BookInventory.model

import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document
data class BookAuditLogs(var id: String?,
                         var title: String?,
                         var price: Long?,
                         var quantity: Int?,
                         var timestamp: String?,
                         var operation:String?
                         )