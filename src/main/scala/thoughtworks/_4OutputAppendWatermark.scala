package thoughtworks

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object _4OutputAppendWatermark {

  def main(args: Array[String]): Unit = {
    // Create Spark Session
    val spark = SparkSession.builder()
      .master("local")
      .appName("Socket Source")
      .getOrCreate()

    // Set Spark logging level to ERROR.
    spark.sparkContext.setLogLevel("ERROR")

    val sourceDF = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", "9999")
      .load()

    import spark.implicits._

    // Split each line into words
    val words = sourceDF
//      .select(explode(split(col("value"), " ")).as("words"))
      .groupBy(
        window($"timestamp", "10 minutes", "5 minutes"),
        $"word"
      )
      .count()

    // Sink
    val sink = words.writeStream
      .outputMode("append")
      .format("console")

    sink.start().awaitTermination()
  }
}
