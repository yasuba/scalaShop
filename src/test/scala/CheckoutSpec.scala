import org.scalatest._

class CheckoutSpec extends WordSpec with Matchers {

  val specialOffers = Map("A" -> List(BigDecimal(3), BigDecimal(-20)), "B" -> List(BigDecimal(2), BigDecimal(-15)))
  val checkout = new Checkout
  val item1 = new SKU("A", 50)
  val item2 = new SKU("B", 30)
  val item3 = new SKU("C", 20)

  "the SKUs" should {
    "have an identifying letter" in {
      item3.itemRef should be("C")
    }

    "have a price" in {
      item3.price should be(20)
    }
  }

  "the checkout" should {
    "allow items to be added" in {
      val basket: List[SKU] = List(item3)
      checkout.totalPrice(basket, specialOffers) should be(20)
    }

    "know that certain items have discounts" in {
      val basket: List[SKU] = item1 :: item1 :: item1 :: Nil
      checkout.totalPrice(basket, specialOffers) should be (130)
    }

    "successfully calculate prices with more than one discount applied" in {
      val basket: List[SKU] = item1 :: item1 :: item1 :: item2 :: item2 :: Nil
      checkout.totalPrice(basket, specialOffers) should be (175)
    }

    "successfully calculate prices when there are more items than a discount specifies" in {
      val basket: List[SKU] = item2 :: item2 :: item2 :: Nil
      checkout.totalPrice(basket, specialOffers) should be (75)
    }

    "successfully apply two lots of the same discount" in {
      val basket: List[SKU] = item2 :: item2 :: item2 :: item2 :: Nil
      checkout.totalPrice(basket, specialOffers) should be (90)
    }

    "calculate a number of different items and discounts" in {
      val basket: List[SKU] = item1 :: item1 :: item1  :: item1 :: item1 :: item1 :: item2 :: item2 :: item2 :: item2 :: item3 :: Nil
      checkout.totalPrice(basket, specialOffers) should be (370)
    }

  }
}
