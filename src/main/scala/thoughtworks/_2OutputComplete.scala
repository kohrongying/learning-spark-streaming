package thoughtworks

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object _2OutputComplete {

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

    // Split each line into words
    val words = sourceDF
      .select(explode(split(col("value"), " ")).as("words"))
      .groupBy("words")
      .count()

    // Sink
    val sink = words.writeStream
      .outputMode("complete")
      .format("console")

    sink.start().awaitTermination()
  }
}
