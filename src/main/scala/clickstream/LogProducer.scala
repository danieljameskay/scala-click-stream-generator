package clickstream

import java.io.FileWriter

import config.Settings

import scala.util.Random

object LogProducer extends App {

  // access the WebLogGen object that contains the settings
  val wlc = Settings.WebLogGen

  // load the products and referrers from the csv's into lines then into arrays
  val Products = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/products.csv")).getLines().toArray
  val Referrers = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/products.csv")).getLines().toArray

  // Creates an array of Visitors and Pages i.e. Visitor-1 and Pages-1, the _ represents the current value
  val Visitors = (0 to wlc.visitors).map("Visitor-" + _)
  val Pages = (0 to wlc.pages).map("Page-" + _)

  // Generate a Random objecy and specify the filepath
  val rnd = new Random()
  val filePath = wlc.filePath

  // Generate a FileWriter and specify the filepath
  val fw = new FileWriter(filePath, true)

  // Introduce some randomness to time increments
  val incrementTimeEvery = rnd.nextInt(wlc.records - 1) + 1

  var timestamp = System.currentTimeMillis()
  var adjustedTimestamp  = timestamp

  for(i <- 1 to wlc.records) {
    adjustedTimestamp = adjustedTimestamp + ((System.currentTimeMillis() - timestamp) * wlc.timeMultipler)
    timestamp = System.currentTimeMillis()
    val action = i % (rnd.nextInt(200) + 1) match {
      case 0 => "purchase"
      case 1 => "add_to_cart"
      case _ => "page_view"
    }
    val referrer = Referrers(rnd.nextInt(Referrers.length - 1))
    val prevPage = referrer match {
      case "Internal" => Pages(rnd.nextInt(Pages.length - 1))
      case _ => ""
    }
    val visitor = Visitors(rnd.nextInt(Visitors.length -1))
    val page = Pages(rnd.nextInt(Pages.length -1))
    val product = Products(rnd.nextInt(Products.length -1))
    val line = s"$adjustedTimestamp\t$referrer\t$action\t$prevPage\t$visitor\t$page\t$product\n"

    fw.write(line)

    if(i % incrementTimeEvery == 0) {
      println(s"Sent $i messages")
      val sleeping = rnd.nextInt(incrementTimeEvery * 60)
      println(s"Sleeping for $sleeping ms")
    }
  }

  fw.close()

}
