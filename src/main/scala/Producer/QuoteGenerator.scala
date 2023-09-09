package Producer
import Config.KafkaConfig

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, ProducerConfig}

import Service.QuoteApi
object QuoteGenerator extends App{

  // Producer properties for the Kafka producer
  val props: Properties = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.CLIENT_ID)
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.KEY_SERIALIZER_CLASS)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.VALUE_SERIALIZER_CLASS)

  //Kafka producer for generating quotes and send them to Kafka
  val quoteGenerator = new KafkaProducer[String, String](props)



  try {

    //Stock Info.
    val stockName = "Alrajhi "
    val stockPrice = "56"

    //Send record to Kafka topic
    quoteGenerator.send(new ProducerRecord[String, String](KafkaConfig.TOPIC, stockName, stockPrice))

  } finally {

    //Close Connection
    quoteGenerator.close()

  }




}
