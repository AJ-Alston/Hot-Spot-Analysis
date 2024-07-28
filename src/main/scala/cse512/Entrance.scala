package cse512

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object Entrance extends App {
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  Logger.getLogger("com").setLevel(Level.WARN)


  val spark = SparkSession
    .builder()
    //enter file path
    .appName()
    .config("spark.executor.memory", "2g")
    .master("local[*]")
    .getOrCreate()


  paramsParser(spark, args)


  private def paramsParser(spark: SparkSession, args: Array[String]): Unit = {
    var paramOffset = 1
    var currentQueryParams = ""
    var currentQueryName = ""
    var currentQueryIdx = -1

    while (paramOffset <= args.length) {
      if (paramOffset == args.length || args(paramOffset).toLowerCase.contains("analysis")) {
        // Turn in the previous query
        if (currentQueryIdx != -1) queryLoader(spark, currentQueryName, currentQueryParams, args(0) + currentQueryIdx)

        // Start a new query call
        if (paramOffset == args.length) return

        currentQueryName = args(paramOffset)
        currentQueryParams = ""
        currentQueryIdx = currentQueryIdx + 1
      }
      else {
        // Keep appending query parameters
        currentQueryParams = currentQueryParams + args(paramOffset) + " "
      }
      paramOffset = paramOffset + 1
    }
  }

  private def queryLoader(spark: SparkSession, queryName: String, queryParams: String, outputPath: String) {
    val queryParam = queryParams.split(" ")
    //println("param length: "+queryParam.length+"  param 1: "+queryParam(0)+"  param 2: "+queryParam(1))

    if (queryName.equalsIgnoreCase("hotcellanalysis")) {
      if (queryParam.length != 1) throw new ArrayIndexOutOfBoundsException("[CSE512] Query " + queryName + " needs 1 parameters but you entered " + queryParam.length)
      HotcellAnalysis.runHotcellAnalysis(spark, queryParam(0)).limit(50).write.mode(SaveMode.Overwrite).csv(outputPath)
    }
    else if (queryName.equalsIgnoreCase("hotzoneanalysis")) {
      if (queryParam.length != 2) throw new ArrayIndexOutOfBoundsException("[CSE512] Query " + queryName + " needs 2 parameters but you entered " + queryParam.length)
      HotzoneAnalysis.runHotZoneAnalysis(spark, queryParam(0), queryParam(1)).write.mode(SaveMode.Overwrite).csv(outputPath)
    }
    else {
      throw new NoSuchElementException("[CSE512] The given query name " + queryName + " is wrong. Please check your input.")
    }
  }
}

