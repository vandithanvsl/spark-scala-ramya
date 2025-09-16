import org.scalatest.funsuite.AnyFunSuite

class DataTransformAndProcessTest extends AnyFunSuite {

  val spark = UserActivityLogParser.spark
  import spark.implicits._

  test("parseDetails correctly parses non-empty JSON string") {
    val json = """{"device": "mobile", "os": "android"}"""
    val parsed = DataTransformAndProcess.parseDetails(json)
    assert(parsed.contains("device" -> "mobile"))
    assert(parsed.contains("os" -> "android"))
  }

  test("parseDetails returns empty for empty JSON") {
    val json = "{}"
    val parsed = DataTransformAndProcess.parseDetails(json)
    assert(parsed.isEmpty)
  }

  test("transformLogs correctly parses example log lines") {
    val lines = Seq(
      """2025-04-29T12:00:00Z, user123, login, {"device": "mobile", "os": "android"}""",
      """2025-04-29T12:05:15Z, user456, view_item, {"item_id": "product789", "duration": 30}""",
      """2025-04-29T12:10:30Z, user123, logout, {}""",
      """2025-04-29T12:12:45Z, user789, add_to_cart, {"item_id": "product123", "quantity": 1}"""
    )
    val rdd = spark.sparkContext.parallelize(lines)
    val tempFile = java.io.File.createTempFile("user_logs", ".txt")
    val pw = new java.io.PrintWriter(tempFile)
    lines.foreach(pw.println)
    pw.close()

    val df = DataTransformAndProcess.transformLogs(tempFile.getAbsolutePath)
    val collectedRows = df.collect()

    // Check counts - should exclude logout record because details empty
    assert(collectedRows.length == 5)

    // Validate schema fields exist
    val schemaFields = df.schema.fields.map(_.name).toSet
    assert(schemaFields.contains("timestamp"))
    assert(schemaFields.contains("user_id"))
    assert(schemaFields.contains("event_type"))
    assert(schemaFields.contains("detail_key"))
    assert(schemaFields.contains("detail_value"))

    tempFile.delete()
  }
}

