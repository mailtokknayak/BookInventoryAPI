package com.inventory.BookInventory.service

import com.inventory.BookInventory.model.Book
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.rule.EmbeddedKafkaRule
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


@RunWith(SpringRunner::class)
@SpringBootTest
@DirtiesContext
class KafkaAuditServiceTest {


    companion object {
        val logger = LoggerFactory.getLogger(KafkaAuditServiceTest::class.java)
        val topic = "bookAudit"

        @ClassRule
        @JvmField
        val embeddedKafkaRule = EmbeddedKafkaRule(1, true, topic)
    }

    @Autowired
    lateinit var kafkaAuditService: KafkaAuditService
    lateinit var container: KafkaMessageListenerContainer<String, String>
    lateinit var records: BlockingQueue<ConsumerRecord<String, String>>

    @Before
    fun setUp() {
        val consumerProperties = KafkaTestUtils.consumerProps("sender",
                "false", embeddedKafkaRule.embeddedKafka)

        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(consumerProperties)
        val containerProperties = ContainerProperties(topic)

        container = KafkaMessageListenerContainer(consumerFactory, containerProperties)
        records = LinkedBlockingQueue()

        container.setupMessageListener(MessageListener<String, String> { record ->
            logger.debug("test-listener received message='{}'", record.toString())
            records!!.add(record)
        })

        container.start()

        ContainerTestUtils.waitForAssignment(container,
                embeddedKafkaRule.embeddedKafka.partitionsPerTopic)
    }

    @After
    fun tearDown() {
        container.stop()
    }


    @Test
    fun `test sendAddMesasge`() {
        val book = Book("1", "Audit Test", listOf("Audit Test"), 100, "Audit Test", 10, null)

        kafkaAuditService.sendAddMessage(book)

        records.poll(10, TimeUnit.SECONDS)
        val auditLog = KafkaAuditService.auditLogs

        assert(auditLog.last().contains("Audit"))
        assert(auditLog.last().contains("BOOK ADDED"))

    }

    @Test
    fun `test sendEditMesasge`() {
        val book = Book("1", "Audit Test", listOf("Audit Test"), 100, "Audit Test", 10, null)

        kafkaAuditService.sendEditMessage(book)

        records.poll(10, TimeUnit.SECONDS)
        val auditLog = KafkaAuditService.auditLogs
        assert(auditLog.last().contains("Audit"))
        assert(auditLog.last().contains("BOOK EDITED"))

    }

    @Test
    fun `test sendDeleteMesasge`() {
        val book = Book("1", "Audit Test", listOf("Audit Test"), 100, "Audit Test", 10, null)

        kafkaAuditService.sendDeleteMessage(book)

        records.poll(10, TimeUnit.SECONDS)
        val auditLog = KafkaAuditService.auditLogs
        assert(auditLog.last().contains("Audit"))
        assert(auditLog.last().contains("BOOK DELETED"))

    }
}