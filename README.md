# OAuth Identifier List Service

## Overview
The OAuth Identifier List Service is a project developed as part of my bachelor's thesis.
It provides a mechanism for managing the status of digital credentials using the
[OAuth Identifier List](https://c2bo.github.io/draft-bormann-identifier-list/draft-bormann-identifier-list.html). 
The implementation enables handling of credential revocation and status verification within applications.

This project was developed in the context of eIDAS 2.0, 
aiming to enhance digital identity management and credential revocation mechanisms.

## Prerequisites
Before setting up the service, ensure your system meets the following requirements:

- **Java**: Version 17.0.12 is required to run the application.
- **Python**: Version 3.13.1 is required if you plan to insert dummy data into the database.
- **Docker & Docker Compose**: Required for setting up the PostgreSQL database.

## Setup Instructions
Follow these steps to set up and run the service:

### 1. Start the PostgreSQL Database
The service requires a PostgreSQL database, which can be started using Docker:
```bash
docker-compose up -d
```

### 2. Launch the Identifier List Service Application
After the database is running, start the `IdentifierListServiceApp` to begin using the service. 
Ensure all dependencies are met before launching the application.

## Adding Dummy Data (Optional)
For testing purposes, you can insert dummy data into the database:

### 1. Install Required Python Package
Install the `psycopg2-binary` package to be able to interact with the database:
```bash
pip install psycopg2-binary
```

### 2. Run the Data Insertion Script
Execute the provided script to fill the database with dummy data:
```bash
python insert_identifiers.py
```
If necessary, modify the script to adjust the volume of inserted data.

## Project Background
This project aims to implement the [OAuth Identifier List](https://c2bo.github.io/draft-bormann-identifier-list/draft-bormann-identifier-list.html)
to enhance the management of digital credentials.
It was developed as part of a bachelor's thesis to analyze and measure the performance of revocation mechanisms for digital credentials.
The performance was measured with JMeter.