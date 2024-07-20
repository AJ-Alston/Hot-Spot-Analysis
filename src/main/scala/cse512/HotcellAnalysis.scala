package cse512

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions.{col, sum, pow, abs, count, udf}
import org.apache.spark.sql.expressions.Window

object HotcellAnalysis {
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  Logger.getLogger("com").setLevel(Level.WARN)

  def runHotcellAnalysis(spark: SparkSession, pointPath: String): DataFrame = {
    // Load the original data from a data source
    var pickupInfo = spark.read.format("com.databricks.spark.csv").option("delimiter", ";").option("header", "false").load(pointPath)
    pickupInfo.createOrReplaceTempView("nyctaxitrips")
    pickupInfo.show()

    // Assign cell coordinates based on pickup points
    spark.udf.register("CalculateX", (pickupPoint: String) => HotcellUtils.CalculateCoordinate(pickupPoint, 0))
    spark.udf.register("CalculateY", (pickupPoint: String) => HotcellUtils.CalculateCoordinate(pickupPoint, 1))
    spark.udf.register("CalculateZ", (pickupTime: String) => HotcellUtils.CalculateCoordinate(pickupTime, 2))
    pickupInfo = spark.sql("SELECT CalculateX(_c5) AS x, CalculateY(_c5) AS y, CalculateZ(_c1) AS z FROM nyctaxitrips")
    pickupInfo.show()

    // Define the min and max of x, y, z
    val minX = -74.50 / HotcellUtils.coordinateStep
    val maxX = -73.70 / HotcellUtils.coordinateStep
    val minY = 40.50 / HotcellUtils.coordinateStep
    val maxY = 40.90 / HotcellUtils.coordinateStep
    val minZ = 1
    val maxZ = 31
    val numCells = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)

    // Count the number of points in each cell
    val cellCounts = pickupInfo.groupBy("x", "y", "z")
      .count()
      .where(col("x").between(minX, maxX) && col("y").between(minY, maxY) && col("z").between(minZ, maxZ))
    cellCounts.show()

    // Calculate global mean and standard deviation
    val stats = cellCounts.agg(
      sum("count").as("total_count"),
      sum(pow(col("count"), 2)).as("count_squared_sum")
    ).collect()(0)

    val totalCount = stats.getAs[Long]("total_count")
    val countSquaredSum = stats.getAs[Double]("count_squared_sum")
    val mean = totalCount.toDouble / numCells
    val stddev = math.sqrt((countSquaredSum / numCells) - (mean * mean))

    // Broadcast the scalar values
    val broadcastMean = spark.sparkContext.broadcast(mean)
    val broadcastStddev = spark.sparkContext.broadcast(stddev)
    val broadcastNumCells = spark.sparkContext.broadcast(numCells)

    // Define a UDF for the G-score calculation
    val calculateGScore = udf((neighborSum: Double, weightSum: Double) => {
      val m = broadcastMean.value
      val s = broadcastStddev.value
      val n = broadcastNumCells.value
      (neighborSum - m * weightSum) / (s * math.sqrt((n * weightSum - math.pow(weightSum, 2)) / (n - 1)))
    })

    // Apply the UDF to calculate G-score
    val hotCells = cellCounts.as("c")
      .join(cellCounts.as("nb"),
        (abs(col("c.x") - col("nb.x")) <= 1) &&
          (abs(col("c.y") - col("nb.y")) <= 1) &&
          (abs(col("c.z") - col("nb.z")) <= 1))
      .groupBy("c.x", "c.y", "c.z")
      .agg(
        sum("nb.count").as("neighbor_sum"),
        count("*").as("weight_sum")
      )
      .withColumn("g_score", calculateGScore(col("neighbor_sum"), col("weight_sum")))
      .orderBy(col("g_score").desc)
      .limit(50)
      .select("c.x", "c.y", "c.z")  // Select only x, y, z columns for final output

    hotCells
  }
}