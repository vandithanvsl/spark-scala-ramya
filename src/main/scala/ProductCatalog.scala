object ProductCatalog {

  /** Compares yesterday's and today's catalogs and categorizes products */
  def categorizeProducts(
                          yestCatalog: Seq[String],
                          todayCatalog: Seq[String]
                        ): (List[String], List[String], List[String], List[String]) = {

    def toMap(catalog: Seq[String]): Map[String, String] =
      catalog.map { line =>
        val productId = line.split(",", 2)(0).trim
        productId -> line.trim
      }.toMap

    val yestMap = toMap(yestCatalog)
    val todayMap = toMap(todayCatalog)

    val yestIds = yestMap.keySet
    val todayIds = todayMap.keySet

    val newlyAdded = todayIds.diff(yestIds).toList.sorted
    val deleted = yestIds.diff(todayIds).toList.sorted

    val commonIds = yestIds.intersect(todayIds)
    val (updated, unchanged) = commonIds.toList.partition { id =>
      yestMap(id) != todayMap(id)
    }

    (newlyAdded, updated, unchanged, deleted)
  }


  def main(args: Array[String]): Unit = {
    val yestCatalog = Seq(
      "101, Awesome Gadget, A truly awesome gadget, 29.99, Electronics",
      "102, Super Widget, The ultimate widget, 19.99, Home Goods",
      "103, Fancy Thingamajig, A very fancy thingamajig, 99.99, Luxury"
    )

    val todayCatalog = Seq(
      "101, Awesome Gadget, Now even more awesome!, 34.99, Electronics",
      "103, Fancy Thingamajig, A very fancy thingamajig, 99.99, Luxury",
      "104, Basic Item, Just a basic item, 5.99, Essentials"
    )

    val (newlyAdded, updated, unchanged, deleted) = categorizeProducts(yestCatalog, todayCatalog)

    println(s"Newly Added: ${newlyAdded.mkString(", ")}")
    println(s"Updated: ${updated.mkString(", ")}")
    println(s"Unchanged: ${unchanged.mkString(", ")}")
    println(s"Deleted: ${deleted.mkString(", ")}")
  }
}

