package Service

import Consumer.TraderActor.logger
import com.typesafe.scalalogging.Logger
import org.joda.time.LocalDate


object TradingService {


  def trade(amount:Double, symbol:String, quote:String): Double = {

    val logger = Logger("Financial-App-Logger")



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
    var stocks = 0


    if(price <= amountTotal){

      if ((price / amount) < 0.10 && price < previousClose && changePercent < 0 &&  changePercent >= -5.0) {

        //Buy quote
        logger.info("********************* Buy Quote *********************")
        logger.info(s"Quote Symbol: ${symbol}")
        logger.info(s"Quote Price: ${price}")
        logger.info(s"Remaining Amount: ${amount}")
        logger.info("The Strategy: This is the best strategy chosen, as it is based on comparing the current price of the quote with its last close, as well as the change percent where should be between 0 and -0.5.")
        logger.info("*****************************************************")

        amountTotal -= price
        stocks += 1

      } else if (price / amount < 0.25  && previousClose - lowPrice < 0  ){


        logger.info("********************* Buy Quote *********************")
        logger.info(s"Quote Symbol: ${symbol}")
        logger.info(s"Quote Price: ${price}")
        logger.info(s"Remaining Amount: ${amount}")
        logger.info("The Strategy: This is the best strategy chosen, as it is based on the differences between the previous close of the quote with its low price and the quote price should be less than 0.25 of the amount.")
        logger.info("*****************************************************")

        amountTotal -= price
        stocks+=1

      }
    }else if(stocks != 0){

       if (previousClose - highPrice > 0) {

         logger.info("********************* Sell Quote *********************")
         logger.info(s"Quote Symbol: ${symbol}")
         logger.info(s"Quote Price: ${price}")
         logger.info(s"Remaining Amount: ${amount}")
         logger.info("The Strategy: This is the best strategy chosen, as it is based on the differences between the previous close of the quote with its high price.")
         logger.info("*****************************************************")

         amountTotal += price
         stocks -= 1


      } else if(price > previousClose){

         logger.info("********************* Sell Quote *********************")
         logger.info(s"Quote Symbol: ${symbol}")
         logger.info(s"Quote Price: ${price}")
         logger.info(s"Remaining Amount: ${amount}")
         logger.info("The Strategy: This is the best strategy chosen, as it is based on the price of the quote where should be greater than previous close.")
         logger.info("*****************************************************")


         amountTotal += price
         stocks -= 1

       }
    }

    return amountTotal;
  }


}
