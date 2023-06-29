## Authors
* Name: Tali Kalev  Email: talikal@edu.hac.ac.il
* Name: Noga Levy Email: levyno@edu.hac.ac.il

## Description

### General information
For this assignment we decided to implement a website where users can browse jokes from a 
Joke API ( https://sv443.net/jokeapi/v2/ ) and save them to their account.
Users can register for account, log in, browse jokes of different categories/types,
save liked jokes, browse and delete from their saved jokes, access account 
information and of course, logout.

The website is built using Spring Boot and Thymeleaf. The website is connected to a MySQL database
where all the user information is stored. The website uses the Joke API to get jokes and categories.

### Functionality
The pages included in the website are:
* Login page - where users can log into their existing account.
* Register page - where users can register for a new account.
* Home page - where users can browse jokes by clicking on generate new (search by category/type) from the API and save 
 them to their account using the heart button.
* Favourites page - where users can browse the jokes they saved to their account and delete them.
* Account page - where users can view their account information and logout.

Additionally, access to the home, favourites, account pages and api requests are authorized to logged in users only 
and the login and register pages are authorized to logged out users only. \
This functionality is implemented using Spring Interceptors.

## Database
* user_info - saves first name, last name, email, password (encrypted)
* favourite - saves joke id and user id \
There is a many to one relation between favourite and user_info

## Installation

1. Create a database named "ex5"
2. Run the project, you should not see any errors in IntelliJ console (you should see 'user_info' and 'favourite' tables)

## Additional Notes
* In order to sign up for an account, email must be unique, first name and last name  must be between 2 and 30 
characters long
* In order to log in, email and password must match an existing account
* Initial search filter is set to "Any" category upon session creation.
* Adding a joke to favourites is done by clicking the heart button on the home page. Deleting the joke from 
favourites is done by clicking the trash button on the favourites page.
* We used an external library for the icons on our page from (fontawesome.com). For this reason, sometimes there is 
a delay in loading them.
* For the offset/limit implementation for the requests to SQL DB to retrieve favourites for favourites page we were
assisted by this website https://blog.felix-seifert.com/limit-and-offset-spring-data-jpa-repositories/