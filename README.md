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
* Home page - where users can browse jokes (by category/type) from the API and save them to their account.
* Favourites page - where users can browse the jokes they saved to their account and delete them.
* Account page - where users can view their account information and logout.

## Database
* user_info - saves first name, last name, email, password (encrypted)
* favourite - saves joke id and user id \
There is a many to one relation between favourite and user_info

## Installation

1. Create a database named "ex5"
2. Run the project, you should not see any errors in IntelliJ console (you should see 'user_info' and 'favourite' tables)


## Useful information

(user, password, etc)

## Additional Notes
* 
* 