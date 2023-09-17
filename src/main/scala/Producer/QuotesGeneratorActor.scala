package Producer

import Config.KafkaConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, Materializer}
import Model.Quote
import akka.Done
import akka.stream.scaladsl.Source
import java.util
import scala.collection.mutable
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Random, Success}
import com.typesafe.scalalogging.Logger


object QuotesGeneratorActor extends App {

  val logger = Logger("Financial-App-Logger")

  logger.info("Start Quotes Generator Actor ...")

  //Creating Actor System
  implicit val financialSystem: ActorSystem = ActorSystem("Financial-App")

  implicit val materializer: Materializer = ActorMaterializer()

  // Producer properties for the Kafka producer
    val props:  util.Map[String, String] = new util.HashMap[String, String]()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.CLIENT_ID)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.KEY_SERIALIZER_CLASS)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.VALUE_SERIALIZER_CLASS)



  val quotesGeneratorSettings = ProducerSettings(financialSystem, new StringSerializer, new StringSerializer).withProperties(props)


  try {

    //List of quotes
    val quoteList: mutable.MutableList[Quote] = generateQuotes()

    logger.info("Sending quotes to Kafka Topic")

    //Send record to Kafka topic
    for (quote <- quoteList) {


      val quoteValues = quote.open + "," + quote.high + "," + quote.low + "," + quote.price + "," + quote.volume + "," + quote.latestTradingDay + "," + quote.previousClose + "," + quote.change + "," + quote.changePercent

      logger.info(s"Quote Symbol --> ${quote.symbol} \n Quote Values --> $quoteValues")

      //Send record to Kafka topic
      val promise: Future[Done] = Source(1 to 100).map(value => new ProducerRecord[String, String](KafkaConfig.TOPIC, quote.symbol, quoteValues)).runWith(Producer.plainSink(quotesGeneratorSettings))


      //Delegate the execution
      implicit val executionContext: ExecutionContextExecutor = financialSystem.dispatcher

      //Handling the result when it success or failure
      promise onComplete {
        case Success(_) =>
          logger.warn(s"Quotes Generator Actor has been terminated successfully.")
          financialSystem.terminate()
        case Failure(error) =>
          logger.error(s"Quotes Generator Actor has been terminated with error --> ${error.toString}")
          financialSystem.terminate()
      }

    }

  } catch {
    case e: Exception => println("Exception occurred during generate quotes " + e)
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
        changePercent = (-1.5 + random.nextDouble * (1.5 - -1.5)).toString)

      //Add quote to quote list
      quoteList += quote
    }

    return quoteList
  }


}