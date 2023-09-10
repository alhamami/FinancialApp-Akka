package Producer
import Config.KafkaConfig
import Model.Quote
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import Service.QuoteApi
import scala.collection.mutable

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

    val quoteList:mutable.MutableList[Quote] = QuoteApi.fetchQuotes();

    //Send record to Kafka topic
    for(quote <- quoteList){

      val values = quote.symbol+","+quote.date+","+quote.open+","+quote.high+","+ quote.low+","+ quote.close+","+quote.volume

      //Send record to Kafka topic
      quoteGenerator.send(new ProducerRecord[String, String](KafkaConfig.TOPIC, values))


    }


  } finally {

    //Close Connection
    quoteGenerator.close()

  }




}
