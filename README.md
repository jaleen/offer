# Offer RESTFUL service
This is a simple REST API based micro service for an offer submission. It provides implementation of GET/POST/PUT/DELETE. 

## Tools and Technologies used
It is primarily based on spring boot, gradle and Java 8. It also uses an in-memory database 

## How to use the application

* Open a command prompt and clone this project. e.g. git@github.com:jaleen/offer.git
* Switch to develop branch. e.g. git checkout master 
* Run the application using command below. This might take a short while if gradle isn't already installed.  
	* ./gradlew clean build bootRun
* Once service is started. Open a REST client e.g. postman. 
* Try adding an offer using POST. Make sure that content-type is selected as application/json. use url something like http://localhost:8080/offers with json body like 
	
	{"description": "c batteries for sale.", "currency":"gbp","price":"1.50"}
