# TweetAppApiSevice

Tweet app api service performs user registartion, user login, post, like, delete, update tweets. This project was built using java 8 and uses MongoDB as database. This project uses spring boot framework version is 2.1.x


## Local Development server
1) Before running app make sure you do **mvn clean install**
2) Run the application
3) If you faced any issue related to define bean for buildproperties, then do **mvn clean package**
4) Run the app again
5) App should be up and running on port 8682.

## Prometheus
prometheus - http://localhost:8071/insights/prometheus  

## Swagger
swagger - http://localhost:8682/api/v1.0/swagger-ui.html#/  


## Code Coverage 
SonarCloud - https://sonarcloud.io/summary/new_code?id=kalyan183_tweet_app_api_service 

## Test cases
used mockito-junit-jupiter 4.5.1 & junit-jupiter-api
