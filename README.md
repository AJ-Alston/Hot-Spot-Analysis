For this project, we need to conduct spatial hot spot analysis. This involves two main tasks:

1. Hot Zone Analysis

  In this task, we'll perform a range join operation between a dataset of rectangles and a dataset of points. For each rectangle, we’ll count the number of points that fall within it. The rectangles with more points are considered "hotter." Our goal here is to determine the "hotness" of all rectangles.

2. Hot Cell Analysis

  In this task, we’ll apply spatial statistics to spatio-temporal big data to identify significant spatial hot spots using Apache Spark. This task is based on the ACM SIGSPATIAL GISCUP 2016 challenge.

Problem Definition: [ACM SIGSPATIAL GISCUP 2016 Problem Definition](http://sigspatial2016.sigspatial.org/giscup2016/problem)

Submission Format: [ACM SIGSPATIAL GISCUP 2016 Submission Format](http://sigspatial2016.sigspatial.org/giscup2016/submit)

Special Requirements (Modifications from GIS CUP)
As detailed in the problem definition, we’ll implement a Spark program to calculate the Getis-Ord statistic for NYC Taxi Trip datasets, which we call "Hot Cell Analysis."

To manage computational requirements, we’ve made the following adjustments:

The input will be a monthly taxi trip dataset from 2009 to 2012 (e.g., "yellow_tripdata_2009-01_point.csv", "yellow_tripdata_2010-02_point.csv").
Each cell unit is sized 0.01 x 0.01 degrees in latitude and longitude.
We’ll use 1 day as the Time Step size, with the first day of a month being step 1. Every month is treated as having 31 days.
Only the Pick-up Location will be considered.
We won't use Jaccard similarity for answer checking, but don't worry about cell coordinates—the provided code template generates them. Your job is to complete the rest of the task.

This project was apart of CSE511 Summer 2024 at Arizona State University
