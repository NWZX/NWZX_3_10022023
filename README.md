# Rental App

This is a prototype of a rental app. It is a full stack application with a front-end and a back-end. The front-end is built with Angular and the back-end with Java Spring Boot.

## Start the project

Git clone:

> git clone https://github.com/NWZX/NWZX_3_10022023

Go inside folder:

> cd NWZX_3_10022023

### Front-end

Go inside folder:

> cd rental_front

Install dependencies:

> npm install

Launch Front-end:

> npm run start;

Open your browser at http://localhost:4200/

### Back-end

Go inside folder:

> cd rental_back

Configure your database connection in `rental_back/src/main/resources/application.properties`

Example if your database name is `rental` and your username is `root` and your password is `root`:

> spring.datasource.url=jdbc:mysql://localhost:3306/rental

> spring.datasource.username=root

> spring.datasource.password=root

Install dependencies:

> mvn install

Launch Back-end:

> mvn spring-boot:run

Endpoints are available at http://localhost:3001/
Swagger is available at http://localhost:3001/v3/api-docs.yaml

## Ressources

### Mockoon env

Download Mockoon here: https://mockoon.com/download/

After installing you could load the environement

> ressources/mockoon/rental-oc.json

directly inside Mockoon

> File > Open environmement

For launching the Mockoon server click on play bouton

Mockoon documentation: https://mockoon.com/docs/latest/about/

### Postman collection

For Postman import the collection

> ressources/postman/rental.postman_collection.json

by following the documentation:

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman

### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

## Authors

* **OpenClassrooms** - *Initial work* - [OpenClassrooms](https://openclassrooms.com/)
* **JoffreyHernandez** - *Front-end* - [JoffreyHernandez](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring)
* **NWZX** - *Back-end* - [NWZX](https://github.com/NWZX)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration from OpenClassrooms, CoPilot, ChatGPT, etc.

