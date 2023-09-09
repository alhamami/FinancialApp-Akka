package Producer

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object QuoteGenerator {

  // Producer properties for the Kafka producer
  val props: Properties = new Properties()

  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  //Kafka producer for generator quotes and send them to Kafka
  val quoteGenerator = new KafkaProducer[String, String](props)


  try {

    //Kafka topic
    val topic = "Quote-Topic"

    val stockName = ""
    val stockPrice = "56"

    //Send record to Kafka topic
    quoteGenerator.send(new ProducerRecord[String, String](, stockName, stockPrice))

  } finally {

    //Close Connection
    quoteGenerator.close()

  }


}
