# spring-example
Spring-based "countries" example

I was asked to create a Web-service which integrated with a third-party API (http://restcountries.eu/rest) that took the data and offere the user a few statistics about the data, namely:
- The countries which are Islands
- The countries which have the most bordering countries, and those countries.

I created a Spring MVC web service utilizing the Java Config paradigm and provided an Angular front-end for the UI. The system uses a basic memory cache for the data retrieved from the API. Additionally, I created a smart-cache for the queries which saved references to the core data set for their results instead of storing duplicate data.

For more information, see the spring-example/WebContent/WEB-INF/html/stats-about.html file
