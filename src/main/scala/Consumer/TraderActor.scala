package Consumer

import Config.KafkaConfig
import Service.TradingService
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import java.util
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object TraderActor extends App {

  val logger = Logger("Financial-App-Logger")

  logger.info("Start Trader Actor ...")

  //Creating Actor System
  implicit val financialSystem: ActorSystem = ActorSystem("Financial-App")

  implicit val materializer: Materializer = ActorMaterializer()

  // Consumer properties for the Kafka consumer
  val props: util.Map[String, String] = new util.HashMap[String, String]()

  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID)
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, KafkaConfig.ENABLE_AUTO_COMMIT)
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, KafkaConfig.AUTO_COMMIT_INTERVAL_MS)
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConfig.KEY_DESERIALIZER_CLASS)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConfig.VALUE_DESERIALIZER_CLASS)


  //Trader settings
  val traderSettings = ConsumerSettings(financialSystem, new StringDeserializer, new StringDeserializer).withProperties(props)

  try{

    logger.info("Reading quotes from Kafka Topic")

    //Read values from Kafka topic and send them to TradingService
    val reader = Consumer.plainSource(traderSettings, Subscriptions.topics("quotes-topic")).runWith(Sink.foreach(T=>TradingService.trade(15000, T.key(), T.value())))

    //Delegate the execution
    implicit val executionContext: ExecutionContextExecutor = financialSystem.dispatcher

    //Handling the result when it success or failure
    reader onComplete {
      case Success(_) =>
        logger.warn(s"Trader Actor has been terminated successfully.")
        financialSystem.terminate()
      case Failure(error) =>
        logger.error(s"Trader Actor has been terminated with error --> ${error.toString}")
        financialSystem.terminate()
    }

  }catch {
    case e: Exception => println("Exception occurred during read quotes " + e)
  }


}
