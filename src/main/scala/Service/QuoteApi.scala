package Service

import Model.Quote
import scalaj.http._
import play.api.libs.json._
import scala.collection.mutable

object QuoteApi extends App{

  //Symbols of companies
  val symbols = List("IBM", "AAPL", "GOOGL", "AMZN", "TSLA")

  //List of quote
  val quoteList = mutable.MutableList[Quote]()

  for(symbol <- symbols){

    try{

      //Fetch quotes
      val response: HttpResponse[String] = Http("https://www.alphavantage.co/query").params(Map("function" -> "TIME_SERIES_DAILY", "symbol" -> symbol, "apikey" -> "demo")).asString
      val json = Json.parse(response.body)

      val timeSeries = (json \\ "Time Series (Daily)")(0)

      //Get keys of quotes
      val keys = getKeys(timeSeries)

      //Fill lsit of quote
      for(key <- keys){
        val quotes = (json \\ key)(0)

        val quote:Quote = new Quote(symbol = symbol, date = key, open = (quotes \\ "1. open")(0).toString(), high = (quotes \\ "2. high")(0).toString(), low = (quotes \\ "3. low")(0).toString(), close = (quotes \\ "4. close")(0).toString(), volume = (quotes \\ "5. volume")(0).toString())
        quoteList += quote

      }

    }catch{
      case e: Exception => println("Exception occurred during fetch quotes " + e)
    }
  }


  //Get keys of json formate
  def getKeys(json: JsValue): collection.Set[String] = json match {
    case o: JsObject => o.keys ++ o.values.flatMap(getKeys)
    case JsArray(as) => as.flatMap(getKeys).toSet
    case _ => Set()
  }


}
