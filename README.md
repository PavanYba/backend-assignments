# User Document Storage Service (UDSS)

UDSS is a REST API service built with Spring Boot for searching, downloading, and uploading files in an Amazon S3 bucket.

## Features

1. **Search Files**: Search for files in the S3 bucket by providing the user's name and a search term.
2. **Download File**: Download a file from the S3 bucket based on the user's name and file name.
3. **Upload File**: Upload a file to the S3 bucket for a specific user.

### Technologies Used

- Java
- Spring Boot
- Amazon S3 (AWS SDK for Java)
- Maven

### Prerequisites

Before running the application, ensure you have the following installed:

- Java JDK
- Maven
- AWS account with S3 bucket configured
- AWS access key and secret key configured on your local machine
