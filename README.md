# Health-Tracker Frontend
This project is the frontend of a health tracker application. It is built using Kotlin and Javalin.

# How it works
The frontend of the health tracker application was built with:
- Kotlin as the programming language
- Javalin as the web framework
- Exposed as the SQL framework
- Jackson for JSON serialization and deserialization of objects
- JUnit for unit testing
- Unirest for HTTP requests
- Maven for dependency management
- Slf4j for logging

The project is organized into the following package structure:

ie.setu.config: contains the JavalinConfig class, which is responsible for starting the service and registering the necessary routes.
ie.setu.service: contains the ApiService class, which handles API calls to the backend.
ie.setu.utils: contains the ErrorExceptionMapping class, which is responsible for handling exceptions, and the JSONUtilities class, which contains utility methods for working with JSON.
ie.setu.auth: contains classes related to authentication and authorization, including AccessManager, which manages access to routes, and Session, which stores and retrieves user information from the session.
ie.setu.domain: contains classes for interacting with the database and managing data, including the Admin class and the AdminAuthParams class.

When the service is started, the JavalinConfig class creates a new Javalin app and registers the necessary routes. Users can navigate to different pages by visiting the corresponding routes. Some routes, such as /login and /logout, have specific functionality. Others, such as /users and /activities, display components built with Vue.js that retrieve and display data from the backend APIs.

The ApiService class handles API calls to the backend. It uses Unirest to make HTTP requests to the backend. The ApiService class is used by the Vue.js components to retrieve and display data.

The ErrorExceptionMapping class is responsible for handling exceptions. It is used by the JavalinConfig class to register the exception handler.

The /api/* route is used to handle API calls. The ApiService class handles incoming requests and makes the necessary calls to the backend APIs to retrieve or modify data.

# Getting Started
## How to run
To run the application, you need to have Java 8 installed. You can then run the application by running the following command in the root directory of the project:
```
mvn clean install
```

## How to test
To run the tests, you need to have Java 8 installed. You can then run the tests by running the following command in the root directory of the project:
```
mvn test
```

# License
This project is licensed under the MIT License - see the LICENSE.md file for details



