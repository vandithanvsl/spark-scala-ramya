import org.apache.spark.sql.{SparkSession, DataFrame, Row}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object DataTransformAndProcess {

  val spark: SparkSession = SparkSession.builder()
    .appName("UserActivityLogParser")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  /** Parses flat JSON string (e.g. {"key":"value", ...}) without external JSON lib */
  def parseDetails(details: String): Seq[(String, String)] = {
    val trimmed = details.trim.stripPrefix("{").stripSuffix("}")
    if (trimmed.isEmpty) Seq.empty
    else {
      trimmed.split(",").toSeq.flatMap { pair =>
        pair.split(":", 2) match {
          case Array(k, v) =>
            // Remove surrounding quotes and trim
            val key = k.trim.stripPrefix("\"").stripSuffix("\"")
            val value = v.trim.stripPrefix("\"").stripSuffix("\"")
            Some(key -> value)
          case _ => None
        }
      }
    }
  }

  // Register UDF for parsing details
  val parseDetailsUDF = udf(parseDetails _)

  /** Reads the log file and transforms it to the required DataFrame */
  def transformLogs(logFilePath: String): DataFrame = {
    val rawLines = spark.read.textFile(logFilePath)

    // Parse CSV: split line on first 3 commas, remaining is details field (may contain commas)
    val parsedDF = rawLines.map { line =>
      val parts = line.split(",", 4)
      val timestamp = parts(0).trim
      val userId = if (parts.length > 1) parts(1).trim else ""
      val eventType = if (parts.length > 2) parts(2).trim else ""
      val details = if (parts.length > 3) parts(3).trim else "{}"
      (timestamp, userId, eventType, details)
    }.toDF("timestamp", "user_id", "event_type", "details")

    parsedDF
      .withColumn("details_kv", parseDetailsUDF($"details"))
      .withColumn("kv", explode($"details_kv"))
      .select(
        $"timestamp",
        $"user_id",
        $"event_type",
        $"kv._1".as("detail_key"),
        $"kv._2".as("detail_value")
      )
  }

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      System.err.println("Usage: UserActivityLogParser <path_to_log_file>")
      System.exit(1)
    }
    val df = transformLogs(args(0))
    df.printSchema()
    df.show(truncate = false)

    spark.stop()
  }
}

