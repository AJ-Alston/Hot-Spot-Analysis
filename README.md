This project conducts spatial hot spot analysis. This involves two main tasks:

1. Hot Zone Analysis

  In this task, we'll perform a range join operation between a dataset of rectangles (path: "./src/resources/zone-hotzone.csv") and a dataset of points (path: "./src/resources/point_hotzone.csv"). For each rectangle, we’ll count the number of points that fall within it by reading points from a csv file and checking if points fall within the rectangles boundaries. The file HotZoneUtils.scala helps perform this operation with the ST_Contains function. The rectangles with more points are considered "hotter." The program returns a folder in the project folder containing a csv file with all of the rectangular cells and how many points lie within them arranged in increasing order.

2. Hot Cell Analysis

  In this task, we’ll apply spatial statistics to spatio-temporal big data to identify significant spatial hot spots using Apache Spark. This task is based on the ACM SIGSPATIAL GISCUP 2016 challenge.

Problem Definition: [ACM SIGSPATIAL GISCUP 2016 Problem Definition](http://sigspatial2016.sigspatial.org/giscup2016/problem)

Submission Format: [ACM SIGSPATIAL GISCUP 2016 Submission Format](http://sigspatial2016.sigspatial.org/giscup2016/submit)

Special Requirements (Modifications from GIS CUP)
As detailed in the problem definition, we’ll implement a Spark program to calculate the Getis-Ord statistic for NYC Taxi Trip datasets, which we call "Hot Cell Analysis."

To manage computational requirements, we’ve made the following adjustments:

The input will be a monthly taxi trip dataset from 2009 (path: "./src/resources/yellow_trip_sample_100000.csv").
Each cell unit is sized 0.01 x 0.01 degrees in latitude and longitude.
We’ll use 1 day as the Time Step size, with the first day of a month being step 1. Every month is treated as having 31 days.
Only the Pick-up Location will be considered.

To run this program download the src file

This project was apart of CSE511 Summer 2024 at Arizona State University
