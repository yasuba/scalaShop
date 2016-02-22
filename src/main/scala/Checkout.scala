import scala.math.BigDecimal.RoundingMode

class SKU(val itemRef: String, val price: BigDecimal)

class Checkout {

  def totalPrice(basket: List[SKU], specialOffers: Map[String, List[BigDecimal]]): BigDecimal = {
    val offers = checkSpecialOffers(basket, specialOffers)
    val discountPrices = getDiscounts(offers, basket).flatten.toList.foldLeft(BigDecimal(0))((sum, elem) => sum + elem)
    subTotal(basket) + discountPrices
  }

  def subTotal(basket: List[SKU]): BigDecimal = {
    val prices = basket.map(_.price)
    prices.foldLeft(BigDecimal(0))((sum, elem) => sum + elem)
  }

  def getDiscounts(discounts: Map[String, List[BigDecimal]], basket: List[SKU]) = {
    for {
      discount <- discounts
      itemCount = BigDecimal(basket.count(_.itemRef == discount._1))
    } yield calculatePrices(itemCount, discount)
  }

  def checkSpecialOffers(basket: List[SKU], specialOffers: Map[String, List[BigDecimal]]): Map[String, List[BigDecimal]] = {
    for {
      discount <- specialOffers
      item <- basket
      if discount._1 == item.itemRef
    } yield discount
  }

  def calculatePrices(itemCount: BigDecimal, discount: (String, List[BigDecimal])): List[BigDecimal] = {
    discount match {
      case x if itemCount == x._2.head => discount._2.tail
      case x if itemCount > x._2.head => multipleDiscounts(itemCount, discount)
      case _ => List(BigDecimal(0))
    }
  }

  def multipleDiscounts(itemCount: BigDecimal, discount: (String, List[BigDecimal])): List[BigDecimal] = {
    List((itemCount / discount._2.head).setScale(0, RoundingMode.DOWN) * discount._2(1))
  }

}
