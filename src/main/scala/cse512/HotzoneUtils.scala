package cse512

object HotzoneUtils {

  def ST_Contains(queryRectangle: String, pointString: String): Boolean = {
    // Split the rectangle and point coordinates from their respective strings
    val rectCoords = queryRectangle.split(",").map(_.trim.toDouble)
    val pointCoords = pointString.split(",").map(_.trim.toDouble)

    // Extract rectangle coordinates
    val rx1 = rectCoords(0)
    val ry1 = rectCoords(1)
    val rx2 = rectCoords(2)
    val ry2 = rectCoords(3)

    // Extract point coordinates
    val px = pointCoords(0)
    val py = pointCoords(1)

    // Check if point (px, py) is within rectangle defined by (rx1, ry1) to (rx2, ry2)
    if (px >= rx1 && px <= rx2 && py >= ry1 && py <= ry2) {
      true
    } else {
      false
    }
  }


  // Helper function to parse coordinates from strings
  //private def parseCoordinates(coordString: String): (Double, Double, Double, Double) = {
  //  val coords = coordString.trim.replaceAll("\\s+", " ").replaceAll("[()]", "").split(" ")
  //  if (coords.length == 4) {
  //    (coords(0).toDouble, coords(1).toDouble, coords(2).toDouble, coords(3).toDouble)
  //  } else {
  //    throw new IllegalArgumentException(s"Invalid coordinate format: $coordString")
  //  }
  //}

}