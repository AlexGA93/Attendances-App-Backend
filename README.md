# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.attendances.Attendances-App-Backend' is invalid and this project uses 'com.attendances.AttendancesAppBackend' instead.

# First steps - Folders Creation
First of all we need to create a number of folders(packages) in our project package (" src/main/java/com.** "):


- models
  - We will define our classes to every model of the project
    - Attendance
    - Classroom
    - Student
- repository
- service
- controller

## Models & MySQL Relations

First of all we need a global model with the info that will be stored in our database. We will declare three models to operate:
- Student **(Every student can own more than one attendance or none of them. Relation with student type: One to many)**
  - id
  - name
  - secondName
  - studentCode
  - Attendance list
  
- Attendance **(Relation with student type: Many to one)**
  - id
  - studentIncome (local date)
  - incomeConfirmation
  - exitConfirmation
  - Student
  
- Classroom
  - id
  - classroomName
  - Student list **(A classroom has many students. Relation with student type: One to many)**


## Connection to Mysql
To connect to the database we need to write the following code in our **resources/application.properties**
```
# Connect to the database url
spring.datasource.url=jdbc:mysql://localhost:3306/db_attendances_spring

# login username of the database ( in this case is a root user)
spring.datasource.username=root

# login with user database's password
spring.datasource.password=root

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update


logging.level.org.hibernate.SQL=debug
```


## Repositories

We are going to create a couple of java interfaces called:
 - "ClassroomRepo"
 - "AttendanceRepo"
 - "StudentRepo"

This interfaces extends from JpaRepository interface to access to the C.R.U.D methods.
It needs to pass our "T" class (in this case is our Attendance class) and a public class as the Id variable ( in this case we declared our user 'Id' as 'private Long')
- example:

  ```
  public interface AttendanceRepo extends JpaRepository<Attendance, Long>{
    /*
    * Method to search by student(student_code) and attendance(studentIncome)
    * Name must follow the next format:
    * findBy + table + _tableAttribute + And + _secondParametter
    * "findByStudent_StudentCodeAndStudentIncome"
    * We pass as arguments our studentCode as "String code" and the LocalDate studentIncome as "LocalDate incomeDate"
    *
    * More info: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
    *
    * Attendance could not exist so it must be optional
    * */
  
    Optional<Attendance> findByStudent_StudentCodeAndStudentIncome(String code, LocalDate incomeDate);
  }
  ```
  
## Service
We're going to create a java class and an interface called:
- **ClassroomService**
- It will host our declared methods to be defined in the 'ClassroomServiceImpl' class:
  ```
  public interface ClassroomService {
    /*
    * Declared methods to define in the implementation class
    * */
    Optional<Classroom> searchClassroomById(Long id);
    Optional<Attendance> searchAttendanceByCode(String code);
    Optional<Student> searchStudentByCode(String code);
  
    void registerAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);
  }
  ```
  - **ClassroomServiceImpl** 
    - It will contain the ClassroomService methods definitions. This code will operate with our data to be stored in the database:
    ```
    public class ClassroomServiceImpl implements ClassroomService{

      // variables with final will be passed through the constructor
      private final AttendanceRepo attendanceRepo;
      private final StudentRepo studentRepo;
      private final ClassroomRepo classroomRepo;


      // ClassroomService Methods to define
  
      @Override
      public Optional<Classroom> searchClassroomById(Long id) {
          // calling Jpa method extended in classroomRepo class to  find by id
          return classroomRepo.findById(id);
      }

      @Override
      public Optional<Attendance> searchAttendanceByCode(String code) {
          // create a local variable wit the actual date
          LocalDate actualDate = LocalDate.now();
          // calling AttendanceRepo method
          return attendanceRepo.findByStudent_StudentCodeAndStudentIncome(code, actualDate);
      }

      @Override
      public Optional<Student> searchStudentByCode(String code) {
          // calling StudentRepo method
          return studentRepo.findByStudentCode(code);
      }

      @Override
      public void registerAttendance(Attendance attendance) {
          // set values to the class
          LocalDate actualDate = LocalDate.now();
          attendance.setStudentIncome(actualDate);
          attendance.setIncomeConfirmation(true);
          attendance.setExitConfirmation(false);
  
          // save into the database
          attendanceRepo.save(attendance);
      }

      @Override
      public void updateAttendance(Attendance attendance) {
          // update an attendance is to inform that student has gone so 'exitConfirmation' must be true
          attendance.setExitConfirmation(true);
  
          // save into the database
          attendanceRepo.save(attendance);
      }
    }

## Controller
In the controller we are going to define all our endpoints and the Http request with the responses:
When we define an endpoint we need to do a previous work:
- Decorators
  ```
    /*
    * Annotation for permitting cross-origin requests on specific handler classes and/or handler methods
    * */
    @CrossOrigin
    /*
  * Generates a constructor with required arguments. Required arguments are final fields and fields with constraints such as @NonNull.
  * */
    @RequiredArgsConstructor
    /*
  * A convenience annotation that is itself annotated with @Controller and @ResponseBody.
    Types that carry this annotation are treated as controllers where @RequestMapping methods assume @ResponseBody semantics by default.
  * */
    @RestController
    /*
    * Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures
    * */
    @RequestMapping("/api/classrooms")
    ```
-  Define a service as final variable
  ```
    // We are going to use our ClassroomService where there are all our methods
    private final ClassroomService classService;
  ```
- Define endpoints
  At this point we need to specify by a decorator our request method and endpoint:
  ```
  // SEARCH CLASSROOM BY ID
    @GetMapping("/search-classroom/{id}") // POST - "/api/classrooms/search-classroom/{id}"
  ```
  Our endpoint will be declared as ResponseEntity<?> (<?> means that it can receive any data type) with the method's name and the arguments.
  ```
  // In this case we want to work with the id passed by the url
  public ResponseEntity<?> searchById(@PathVariable Long id){}
  ```
  **IMPORTANT:** We can define a single variable as path variable or a body as payload (for example in a POST request). This last one will be defined as "@RequestBody <type> variable_name"
  ```
  public ResponseEntity<?> updateAttendance(@RequestBody Attendance attendance){...}
  ```
  Inside our endpoint will be the code that allows to deal with the information, validate data and send http request responses:
  ```
  @GetMapping("/search-classroom/{id}") // POST - "/api/classrooms/search-classroom/{id}"
    public ResponseEntity<?> searchById(@PathVariable Long id){
        // calling our classroom service method and store in a local variable
        Optional<Classroom> foundClassroom = classService.searchClassroomById(id);

        // validate if there is a found classroom
        if(foundClassroom.isEmpty()){
            String statusMessage = "Classroom not found!";
            return new ResponseEntity<>(statusMessage, HttpStatus.NOT_FOUND);
        }

        // if a classroom is found return it = HTTP status code 200
        return new ResponseEntity<>(foundClassroom.get(), HttpStatus.OK);

    }
  ```


# Spring Project - Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans ??? insights for your project's build](https://scans.gradle.com#gradle)

