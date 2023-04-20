# E-Store:  RIT Garage Sale

An online E-store system built in Java 18 with Angular and Spring Boot that uses MongoDB Atlas for storage.
  
## Team

- Jacob Karvelis
- Zach Brown
- Kelly Showers
- Ethan Meyers
- Andrew Bush


## Prerequisites

- Java 8=>11 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
- Credentials for MongoDB Atlas stored in the application.properties file


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. Go to estore-api folder in the project
3. Execute command: mvn compile exec:java
4. Go to estore-ui folder in the project
5. Execute command: ng serve --open
6. E-store website should open automatically in browser for running/testing
Website URL should be by default: 'http://localhost:8080/'


## Known bugs and disclaimers

There are consistency issues with the storage of the current user in MongoDB and the user IDs associated with 
shopping carts that are stored in the carts.json file in the GitHub repostiory. 
This will break the shopping cart functionality for certain users.


## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## License

MIT License

See LICENSE for details.
