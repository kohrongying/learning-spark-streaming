package thoughtworks

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types._

object _6Checkpointing {

  def main(args: Array[String]): Unit = {
    // Create Spark Session
    val spark = SparkSession.builder()
      .master("local")
      .appName("File Source With Checkpointing")
      .getOrCreate()

    // Set Spark logging level to ERROR.
    spark.sparkContext.setLogLevel("ERROR")

    val schema = StructType(List(
      StructField("#", StringType, true),
      StructField("Name", StringType, true),
      StructField("Type 1", StringType, true),
      StructField("Type 2", StringType, true),
      StructField("Total", DoubleType, true),
      StructField("HP", DoubleType, true),
      StructField("Attack", DoubleType, true),
      StructField("Defense", DoubleType, true),
      StructField("Sp. Atk", DoubleType, true),
      StructField("Sp. Def", DoubleType, true),
      StructField("Speed", DoubleType, true),
      StructField("Generation", DoubleType, true),
      StructField("Legendary", BooleanType, true)
    ))

    val sourceDF = spark.readStream
      .format("csv")
      .option("maxFilesPerTrigger", 2) // This will read maximum of 2 files per mini batch. However, it can read less than 2 files.
      .option("header", true)
      .schema(schema)
      .load("data/pokemon*.csv") // asterisk is important here

    // Do some transform
    val pokemon = sourceDF
      .groupBy("Type 1")
      .agg(max("HP").as("Tank"))

    // Sink
    val sink = pokemon.writeStream
      .outputMode(OutputMode.Update)
      .format("console")
      .option("checkpointLocation", "chkpt") // necessary for checkpointing

    sink.start().awaitTermination()
  }
}
