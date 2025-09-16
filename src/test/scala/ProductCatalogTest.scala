import org.scalatest.funsuite.AnyFunSuite

class ProductCatalogTest extends AnyFunSuite { 


  test("categorize products correctly") {
    val yesterday = Seq(
      "101, Awesome Gadget, A truly awesome gadget, 29.99, Electronics",
      "102, Super Widget, The ultimate widget, 19.99, Home Goods",
      "103, Fancy Thingamajig, A very fancy thingamajig, 99.99, Luxury"
    )

    val today = Seq(
      "101, Awesome Gadget, Now even more awesome!, 34.99, Electronics",
      "103, Fancy Thingamajig, A very fancy thingamajig, 99.99, Luxury",
      "104, Basic Item, Just a basic item, 5.99, Essentials"
    )

    val (newlyAdded, updated, unchanged, deleted) = ProductCatalog.categorizeProducts(yesterday, today)

    assert(newlyAdded == List("104"))
    assert(updated == List("101"))
    assert(unchanged == List("103"))
    assert(deleted == List("102"))
  }
}

