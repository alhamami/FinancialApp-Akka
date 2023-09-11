package Service

import Model.Quote
import scalaj.http._
import play.api.libs.json._
import scala.collection.mutable

object QuoteApi{


  //Fetch quote
  def fetchQuotes(): mutable.MutableList[Quote] = {

    //Symbols of companies
    val symbols = List("IBM", "AAPL", "GOOGL", "AMZN", "TSLA")

    //List of quote
    val quoteList = mutable.MutableList[Quote]()

    for (symbol <- symbols) {

      try {

        //Fetch quotes
        val response: HttpResponse[String] = Http("https://www.alphavantage.co/query").params(Map("function" -> "GLOBAL_QUOTE", "symbol" -> symbol, "apikey" -> "tbd1")).asString
        val json = Json.parse(response.body)


        //Fetched quote
        val globalQuote = (json \\ "Global Quote")(0)

        //Quote fields
        val quoteSymbol = (globalQuote \\ "01. symbol")(0).toString().replace("\"", "")
        val quoteOpen = (globalQuote \\ "02. open")(0).toString().replace("\"", "")
        val quoteHigh = (globalQuote \\ "03. high")(0).toString().replace("\"", "")
        val quoteLow = (globalQuote \\ "04. low")(0).toString().replace("\"", "")
        val quotePrice = (globalQuote \\ "05. price")(0).toString().replace("\"", "")
        val quoteVolume = (globalQuote \\ "06. volume")(0).toString().replace("\"", "")
        val quoteLatestTradingDay = (globalQuote \\ "07. latest trading day")(0).toString().replace("\"", "")
        val quotePreviousClose = (globalQuote \\ "08. previous close")(0).toString().replace("\"", "")
        val quoteChange = (globalQuote \\ "09. change")(0).toString().replace("\"", "")
        val quoteChangePercent = (globalQuote \\ "10. change percent")(0).toString().replace("\"", "")

        //Create Quote object
        val quote: Quote = Quote(symbol = quoteSymbol, open = quoteOpen, high = quoteHigh, low = quoteLow, price = quotePrice, volume = quoteVolume, latestTradingDay = quoteLatestTradingDay, previousClose = quotePreviousClose, change = quoteChange, changePercent = quoteChangePercent)

        //Add quote to quote list
        quoteList += quote

      } catch {
        case e: Exception => println("Exception occurred during fetch quotes " + e)
      }
    }
    return quoteList;
  }

}
