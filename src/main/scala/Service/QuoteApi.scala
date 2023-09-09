package Service



import scalaj.http._
import play.api.libs.json._
object QuoteApi extends App{

  val symbols = List("IBM", "AAPL", "GOOGL", "AMZN", "TSLA")

  for(symbol <- symbols){

    val response: HttpResponse[String] = Http("https://www.alphavantage.co/query").params(Map("function" -> "TIME_SERIES_DAILY", "symbol" -> symbol, "apikey" -> "tbd1")).asString
    val json = Json.parse(response.body)

    //val metaData = (json \\ "Meta Data")(0)
    //val companySymbol = (metaData \\ "2. Symbol")(0)
    //val data = (json \\ "Time Series (5min)")(0)

    println(s"${symbol} Data: "+json)


  }





}
