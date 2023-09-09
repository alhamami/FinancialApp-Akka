package Consumer

import Config.KafkaConfig

import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.collection.JavaConverters._
object Trader extends App{


  val props: Properties = new Properties()

  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID)
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, KafkaConfig.ENABLE_AUTO_COMMIT)
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, KafkaConfig.AUTO_COMMIT_INTERVAL_MS)
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConfig.KEY_DESERIALIZER_CLASS)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConfig.VALUE_DESERIALIZER_CLASS)


  //Kafka consumer for consuming quotes from Kafka
  val trader = new KafkaConsumer(props)


  try {

    //Subscribe to Kafka Topic
    trader.subscribe(List(KafkaConfig.TOPIC).asJava)

    //Loop to keep check Kafka if there is a new quote
    while (true) {

      //Read quotes from Kafka
      val quotes = trader.poll(2000).asScala

      //Loop through each quote in quotes
      for (data <- quotes) {

        //Print quote key and value
        println("# Key: " + data.key())
        println("# Value: " + data.value())

      }
    }
  } finally {

    //Close Connection
    trader.close()
  }

}
