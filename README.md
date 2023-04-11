
# Crypto Recommendation Service

This service fetches data from csv files related to crypto prices for certain symbols at given times. Based on this data, it is able to calculate the min/max/oldest/newest prices for each symbol, and also calculate the normalized range.

Currently, 3 endpoints are exposed:
Get data for all symbols, sorted descending from highest to lowest normalized range
Get data for a specific symbol for the whole time period, which we have data for
Get data for a specific date, returning the symbol with the highest normalized range


## Configuration

We use a total of 4 configuration parameters:
`prices.filepath` in properties or
`PRICES_FILEPATH` in environment variables: Path to look for the CSVs containing crypto currency pricing data.

Examples:
classpath:*_values.csv (default)
file:C:\cryptodata\prices\*.csv (from external file system).

Make sure the path is correct and that the process has access to it, otherwise startup will fail.

`prices.refresh.interval` in properties or `PRICES_REFRESH_INTERVAL` in environment variables: How often to re-read 
the data from disc in seconds. 3600 (1 hour) default if not specified.

`rate.limit` in properties or `RATE_LIMIT` in environment variables: Amount of allowed API requests 
for a source IP in the specified time interval (cumulative for all APIs).

`rate.limit.duration` in properties or `RATE_LIMIT_DURATION` in environment variables: Duration in minutes 
for the specified rate limit.

## Swagger
After running the application, swagger documentation is available on (assuming default port): http://localhost:8080/swagger-ui

## Running the application
To run the application locally, just do:
mvn clean install
java -jar interview-task-0.0.1-SNAPSHOT.jar

For docker:
docker build --tag=crypto-recommendations:0.1a .
docker run -p 8080:8080 crypto-recommendations:0.1a

or just:
docker-compose up -d
which will run it on port 8080, to change the port - update docker-compose.yml

You can change the 1st port number to a value of your choice, in case it's taken.