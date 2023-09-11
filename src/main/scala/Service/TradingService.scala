package Service

import org.joda.time.LocalDate


object TradingService {


  def trade(amount:Double, symbol:String, quote:String): Double = {


    val quoteValues:Array[String] = quote.split(",")


    val openPrice:Double = quoteValues(0).toDouble
    val highPrice:Double = quoteValues(1).toDouble
    val lowPrice:Double = quoteValues(2).toDouble
    val price:Double = quoteValues(3).toDouble
    val volume:Double = quoteValues(4).toDouble
    val latestTradingDay: LocalDate = LocalDate.parse(quoteValues(5))
    val previousClose:Double = quoteValues(6).toDouble
    val change:Double = quoteValues(7).toDouble
    val changePercent:Double = quoteValues(8).split("%")(0).toDouble

    var amountTotal = amount
    var stocks = 4


    if(price <= amountTotal){

      if ((price / amount) < 0.10 && price < previousClose && changePercent < 0 &&  changePercent >= -5.0) {
        //Buy
        println("## Buy")
        amountTotal -= price
        stocks += 1

        println("##########################################################")
        println(amountTotal +" -- "+ stocks)
        println("##########################################################")

      } else if (price / amount < 0.25 && latestTradingDay.compareTo(LocalDate.now)  == 1  && previousClose - lowPrice < 0  ){
        println("## Buy")

        amountTotal -= price
        stocks+=1

        println("##########################################################")
        println(amountTotal +" -- "+ stocks)
        println("##########################################################")

      }
    }else if(stocks != 0){

       if (latestTradingDay.compareTo(LocalDate.now) == 1 && previousClose - highPrice > 0) {
        println("## Sell")
        amountTotal += price
        stocks -= 1

         println("##########################################################")
         println(amountTotal + stocks)
         println("##########################################################")


      } else if(price > previousClose){
         println("## Sell")
         amountTotal += price
         stocks -= 1

         println("##########################################################")
         println(amountTotal + stocks)
         println("##########################################################")

       }
    }

    return amountTotal
  }


}
