package com.inventory.BookInventory.service


import com.inventory.BookInventory.model.Book
import org.springframework.stereotype.Service
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList


@Service
class KafkaAuditService {

    @Autowired
     var kafkaTemplate: KafkaTemplate<String, String>? = null




    companion object{
        var auditLogs: ArrayList<String> = ArrayList()
        var saperator: String = "-->"
    }

    fun sendAddMessage(book: Book) {
        this.kafkaTemplate!!.send("bookAudit", prepareMessage("BOOK ADDED",book))
    }

    fun sendEditMessage(book: Book) {
        this.kafkaTemplate!!.send("bookAudit", prepareMessage("BOOK EDITED",book))
    }

    fun sendDeleteMessage(book: Book) {
        this.kafkaTemplate!!.send("bookAudit", prepareMessage("BOOK DELETED",book))
    }

    @KafkaListener(topics = ["bookAudit"], groupId = "group_id")
    fun consume(message: String)  {
        println(message)
        auditLogs.add(message);
    }

    private fun getTimeStamp(): String{
        return Timestamp(Date().time).toString()
    }

    private fun prepareMessage(operation: String,book: Book): String? {
//        return getTimeStamp() + saperator + operation + saperator +
//                "{id : ${book.id}, name: ${book.title}, price: ${book.price}, quantity: ${book.quantity} }"


           var time: String = getTimeStamp()

        return  "   { \n" +
                "   \t\"id\":\"${book.id}\" ,\n" +
                "     \"title\":\"${book.title}\" ,\n" +
                "     \"price\":\"${book.price}\" ,\n" +
                "     \"quantity\":\"${book.quantity} \" ,\n" +
                "     \"timeStamp\":\"$time \" ,\n" +
                "     \"operation\":\"$operation \"    }"




    }

}