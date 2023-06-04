


Credit Application System with Spring Boot

- #### Backend live on [http://3.127.150.26:8080/swagger-ui/index.html#/](http://3.127.150.26:8080/swagger-ui/index.html#/)

---

#### Login information
In order to test endpoints with admin role, a user with role admin authority is below.

- tcIdentityNumber: 98745632113
- password: 123456789

---

## Used Technologies

- Java 11
- Spring Boot
- Spring Data JPA
- Restful API
- Swagger documentation
- Spring security(JWT)
- Lombok
- Exception Handling
- H2 database (test)
- Mysql (prod and dev)
- Spring Profiles
- unit test(JUnit Mockito)
- logging (SLF4J)
- integration test(TestContainers)
- Maven
- CI/CD pieline (CircleCI)
- Docker
- AWS ECS
- Redis
- RabbitMQ

## build 
 git clone https://github.com/yusuf4152/CreditApplicationSystem.git
 
- $ cd CreditApplicationSystem
- $ docker-compose up 
- $ mvn clean install
- $ mvn spring-boot:run

## Swagger UI will be run on this url
`http://localhost:8080/swagger-ui.html`
## api endpoints
![image](https://user-images.githubusercontent.com/55889339/220599074-6b20b82b-bb42-43f1-bdc6-ebadecb5bbf5.png)


