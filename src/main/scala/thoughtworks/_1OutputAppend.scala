package thoughtworks

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.functions._

object _1OutputAppend {

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
      .withColumn("count", lit(1))

    // Sink
    val sink = words.writeStream
      .outputMode("append")
      .format("console")

    sink.start().awaitTermination()
  }
}
