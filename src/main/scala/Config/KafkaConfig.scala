package Config
import org.apache.kafka.common.serialization.StringSerializer
object KafkaConfig {

  val BOOTSTRAP_SERVERS = "localhost:9092"
  val TOPIC= "Quote-Topic"
  val CLIENT_ID = "QuoteGenerator"
  val KEY_SERIALIZER_CLASS = StringSerializer
  val VALUE_SERIALIZER_CLASS  = StringSerializer

}
