# employee-portal with REST API's

## Software requirements
- Java version 1.8
- Spring Boot 2.1.8.RELEASE
- MongoDb 4.0.12 or above 
- Maven 3.0.4 or above

## Build the project
checkout and run the following command in employee-portal folder
- mvn clean install

For executing unit tests run the following command in employee-portal folder
- mvn test

Set application configuration details in employee-portal/src/main/resources/application.properties

configure server port by default it is 8080
- server.port=8080

Set mongoDB connection details for persistence

by default following values are set 

- spring.data.mongodb.host=localhost
- spring.data.mongodb.port=27017
- spring.data.mongodb.database=employeesDB

Set log configuration properties in employee-portal/src/main/resources/log4j2.properties

by default logging will be done in employee-portal/logs/employeePortal.log

To launch the sever execute following command in employee-portal folder

- java -jar target/employee-portal-0.0.1-SNAPSHOT.jar

To add an employee send following REST request along with JSON body as sample provided below

- POST http://localhost:8080/employees/
```
Body

{
    "firstName": "Deepash",
    "lastName": "",
    "gender": "M",
    "dateOfBirth": "1-10-1981",
	"department": "Foundation Engg"
}

response:

HTTP status code - 200

Body

{
    "_id": "5d81a6d312002fdb474f9c98",
    "firstName": "Deepash",
    "lastName": "M R",
    "gender": "M",
    "dateOfBirth": "01-10-1981",
    "department": "Foundation Engg"
}
```
Update an employee send following REST request along with JSON body as sample provided below

- PUT http://localhost:8080/employees/{Employee Id}
```
Body

{
    "firstName": "Deepash",
    "lastName": "Rao",
    "gender": "M",
    "dateOfBirth": "1-10-1981",
	"department": "Foundation Engg"
}

response:
HTTP status code - 200
```
Get All employees will fetch employees sorted on first name with pagination by default page 0 and 10 items. Send following REST request as sample provided below to get page 0 and 2 items. 

- GET http://localhost:8080/employees/?pageId=0&count=2
```
response:
HTTP status code - 200

Body

[
    {
        "_id": "5d81a6d312002fdb474f9c98",
        "firstName": "Deepash",
        "lastName": "Rao",
        "gender": "M",
        "dateOfBirth": "01-10-1981",
        "department": "Foundation Engg"
    },
    {
        "_id": "5d7ddcb30b7e6bce0c7355cb",
        "firstName": "Ravi",
        "lastName": "Deshpande",
        "gender": "M",
        "dateOfBirth": "11-10-1987",
        "department": "Marketing"
    }
]
```
Get single employee send following REST request as sample provided below

- GET http://localhost:8080/employees/{Id}
```
response:

HTTP status code - 200

Body

{
    "_id": "5d81a6d312002fdb474f9c98",
    "firstName": "Deepash",
    "lastName": "Rao",
    "gender": "M",
    "dateOfBirth": "01-10-1981",
    "department": "Foundation Engg"
}
```
To remove an employee send following REST request as sample provided below

- DELETE http://localhost:8080/employees/{Id}
```
response:

HTTP status code - 200
```
