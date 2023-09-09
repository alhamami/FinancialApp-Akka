package Config

object KafkaConfig {

  val stringSerializer =  "org.apache.kafka.common.serialization.StringSerializer"
  val stringDeserializer = "org.apache.kafka.common.serialization.StringDeserializer"

  //List of host/port pairs to use for establishing the initial connection to the Kafka cluster
  val BOOTSTRAP_SERVERS = "localhost:9092"
  //Name of Kafka topic logs that hold messages and events in a logical order
  val TOPIC = "Quote-Topic"
  //To keep tracking the source of requests
  val CLIENT_ID = "QuoteGenerator"
  //To determines which consumers belong to which group
  val GROUP_ID = "Trading"
  //To commit offsets to avoid restart from the oldest stored records every time
  val ENABLE_AUTO_COMMIT = "true"
  //Interval for commit offsets
  val AUTO_COMMIT_INTERVAL_MS = "1000"
  //To converts the key into bytes before the producer sends the message to the topic
  val KEY_SERIALIZER_CLASS = stringSerializer
  //To converts the value into bytes before the producer sends the message to the topic
  val VALUE_SERIALIZER_CLASS  = stringSerializer
  //To converts the key from bytes to string
  val KEY_DESERIALIZER_CLASS = stringDeserializer
  //To converts the value from bytes to string
  val VALUE_DESERIALIZER_CLASS = stringDeserializer

}
