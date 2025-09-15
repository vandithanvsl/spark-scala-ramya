import org.apache.spark.sql.SparkSession

object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("MySparkApp")
      .getOrCreate()

    println("Spark Application Initialized: " + spark.sparkContext.appName)
    spark.stop()
  }
}
