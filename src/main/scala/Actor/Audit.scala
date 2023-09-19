package Actor

import Model.QuoteRecord
import akka.actor.Actor
import com.typesafe.scalalogging.Logger

class Audit extends Actor{

  val logger = Logger("Financial-App-Logger")

  logger.info("Start Audit Actor ...")

  override def receive: Receive = {
    case quoteRecord: QuoteRecord =>
      if(!quoteRecord.symbol.equalsIgnoreCase("") && quoteRecord.price >= 0 && quoteRecord.action.equalsIgnoreCase("Buy")){

        logger.info("Order has been validated and stored.")

      }else if(!quoteRecord.symbol.equalsIgnoreCase("") && quoteRecord.price >= 0 && quoteRecord.action.equalsIgnoreCase("Sell")){
        logger.info("Order has been validated and stored.")
      }

    case _ =>  logger.error("Order is invalid.")

  }

}
