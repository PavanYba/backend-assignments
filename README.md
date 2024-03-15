Pincode Distance Calculator

Pincode Distance Calculator is a Java Spring Boot application that provides a RESTful API for calculating distances, durations, and routes between two locations specified by pin codes. It integrates with the Google Maps Directions API to fetch accurate route information and stores this data in a MySQL database for caching and future retrieval.

Distance Calculation: Calculate the distance between two locations specified by pin codes.
Duration Calculation: Get the estimated travel duration between two locations.
Route Information: Retrieve detailed step-by-step route instructions.
Caching: Cache route information to minimize API calls to Google Maps.
MySQL Database: Store route information, distances, durations, and coordinates.
Polygon Information: Save and retrieve polygon information for a route.

Technologies Used
Java
Spring Boot
MySQL
Google Maps Directions API
Maven

..........................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................

User Document Storage Service (UDSS)
UDSS is a REST API service built with Spring Boot for searching, downloading, and uploading files in an Amazon S3 bucket.

Features
Search Files: Search for files in the S3 bucket by providing the user's name and a search term.
Download File: Download a file from the S3 bucket based on the user's name and file name.
Upload File: Upload a file to the S3 bucket for a specific user.
Technologies Used
Java
Spring Boot
Amazon S3 (AWS SDK for Java)
Maven
Prerequisites
Before running the application, ensure you have the following installed:

Java JDK
Maven
AWS account with S3 bucket configured
AWS access key and secret key configured on your local machine
