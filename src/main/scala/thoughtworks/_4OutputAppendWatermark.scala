package thoughtworks

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode

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
      .option("includeTimestamp",  true) // necessary for watermarking
      .load()

    import spark.implicits._

    // Split each line into words
    val words = sourceDF
      .select(explode(split(col("value"), " ")).as("words"), col("timestamp"))

    val windowedCounts = words
      .withWatermark("timestamp", "2 minutes") // late arrivals of up to 2 mins
      .groupBy("words")
      .count()

    // Sink
    val sink = windowedCounts.writeStream
      .outputMode(OutputMode.Update)
      .format("console")

    sink.start().awaitTermination()
  }
}
