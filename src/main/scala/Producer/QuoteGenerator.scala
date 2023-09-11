package Producer
import Config.KafkaConfig
import Model.Quote

import java.util.{Date, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import Service.QuoteApi

import java.util.concurrent.ThreadLocalRandom
import scala.collection.mutable
import scala.util.Random

object QuoteGenerator extends App{

  // Producer properties for the Kafka producer
  val props: Properties = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.CLIENT_ID)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.KEY_SERIALIZER_CLASS)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.VALUE_SERIALIZER_CLASS)

  //Kafka producer for generating quotes and send them to Kafka
  val quoteGenerator = new KafkaProducer[String, String](props)



  try {


    val quoteList:mutable.MutableList[Quote] =  generateQuotes()  //QuoteApi.fetchQuotes();

    //Send record to Kafka topic
    for(quote <- quoteList){


      val quoteValues = quote.open+","+quote.high+","+ quote.low+","+ quote.price+","+quote.volume+","+quote.latestTradingDay+","+quote.previousClose+","+quote.change+","+quote.changePercent

      //Send record to Kafka topic
      quoteGenerator.send(new ProducerRecord[String, String](KafkaConfig.TOPIC, quote.symbol, quoteValues))

    }


  }catch {
    case e: Exception => println("Exception occurred during  generate quotes " + e)
  }finally {

    //Close Connection
    quoteGenerator.close()

  }


  //Generate quotes
  def generateQuotes(): mutable.MutableList[Quote] = {

    //Symbols of companies
    val symbols = List("IBM", "AAPL", "GOOGL", "AMZN", "TSLA")

    //List of quote
    val quoteList = mutable.MutableList[Quote]()

    val random = new scala.util.Random

    for (i <- 0 to 100) {

      val quote: Quote = Quote(symbol = symbols(Random.nextInt(4 - 0) + 0), open = (100 + random.nextDouble * (200 - 100)).toString,
        high = (100 + random.nextDouble * (200 - 100)).toString,
        low = (100 + random.nextDouble * (200 - 100)).toString,
        price = (100 + random.nextDouble * (200 - 100)).toString,
        volume = (Random.nextInt(4000000 - 1000000) + 1000000).toString,
        latestTradingDay = "2023-09-11", previousClose = (100 + random.nextDouble * (200 - 100)).toString,
        change = (-3 + random.nextDouble * (5 - -3)).toString,
        changePercent = (-1.5 + random.nextDouble * (1.5 - -1.5 )).toString)

      //Add quote to quote list
      quoteList += quote
    }

    return quoteList
  }

}
