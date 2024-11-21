# Project goal:

The goal of this project is to develop a Roles App that allows managing roles and memberships within teams. The
application provides functionalities to create, retrieve, update, and delete roles, as well as assigning roles to team
members through memberships. The project aims to demonstrate a well-structured and scalable application architecture
using Spring Boot and Kotlin.

## Architecture decisions:

### Microservices Architecture:

The application is designed as a microservice architecture, with individual services responsible for specific
functionalities. This allows for scalability, independent deployment, and easy maintenance of each service.

## Design patterns:

### Repository Pattern:

The repository pattern is used to abstract the data access layer and provide a consistent interface for working with
data. Repositories encapsulate the logic for querying and persisting entities, enabling separation of concerns and
promoting testability.

### DTO (Data Transfer Object):

DTOs are used to transfer data between layers and services. They provide a clear and structured representation of data,
separate from the domain entities, allowing for flexibility in defining the data structure exchanged between different
components.

## Swagger-UI

The application provides Swagger-UI integration for easy exploration and testing of the RESTful APIs.

## Observability:

The project incorporates observability features to gain insights into the application's behavior and performance:

### Logging:

The application utilizes logging frameworks, such as SLF4J and Logback, to record important events and messages. Log
statements are strategically placed to capture relevant information for troubleshooting and auditing purposes.

### Metrics and Monitoring:

The application integrates with Micrometer to collect and expose metrics about its behavior, performance, and resource
utilization. These metrics can be consumed by monitoring systems like Prometheus or visualized in tools like Grafana.

### Distributed Tracing:

The application integrates with distributed tracing systems like Zipkin or Jaeger to provide end-to-end visibility into
requests and transactions across different services. It enables tracking request flows, identifying bottlenecks, and
diagnosing performance issues.

## Spring Boot Admin:

The Spring Boot Admin module is utilized to monitor and manage the application during runtime. Application Monitoring:
Spring Boot Admin provides a web-based user interface to monitor and manage the application instances. It displays
information such as health checks, JVM metrics, and environment details for each running instance.

- Logging and Log Level Management: Spring Boot Admin allows viewing and managing the log files of the application
  instances. It provides log aggregation capabilities and the ability to adjust the log levels dynamically without
  restarting the application.

- Alerting and Notifications: Spring Boot Admin supports configuring alerts and notifications based on application
  health status or other custom metrics. It can send notifications via email, Slack, or other channels to inform
  administrators about critical events or performance issues.

## DB model

To adhere to the microservice principle of data encapsulation, the microservice primarily retains the minimal
necessary information for its specific domain. This includes storing the IDs of the team and user (member) entities
rather than duplicating all the detailed data associated with them. By referencing the IDs, the microservice
can establish the connections between teams, users, roles, and memberships while avoiding data redundancy.

```SQL
CREATE TABLE roles (
    id UUID DEFAULT RANDOM_UUID() NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE membership (
    id UUID DEFAULT RANDOM_UUID() NOT NULL,
    member_id UUID,
    role_id UUID,
    team_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_membership_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);
```

## Liquibase:

The project incorporates Liquibase, an open-source database migration tool, to manage database schema changes and ensure
consistent database state across different environments.

Liquibase allows the application to define database schema changes using a declarative format, which can be versioned
and applied in a controlled manner. The changes are organized into changesets, each representing a specific database
change.

Check the files at `src/main/resources/db/changelog/migration`

## Security

#### Justification for not including Spring Security in the assessment project:

Due to the time constraints of this assessment, I have decided not to include Spring Security in the project at the
moment. Implementing Spring Security 6.x requires a thorough understanding of its latest version and best practices to
ensure proper configuration and secure authentication and authorization mechanisms.

## Caching

The application utilizes caching to improve performance and reduce the load on the underlying data sources. Spring
Framework provides caching support through the `@Cacheable` annotation.

## Tests

There are Unit and Integration tests in the project but given the lack of time, not all the scenarios are covered, but
in fact there are all the features and setup do add them all.

Ps.: The project is using `WireMock` to stub the external API calls

### Initial Roles in the System:

The project starts with three predefined roles:

Developer: Represents the role of a software developer responsible for coding and implementing features.

Product Owner: Represents the role of a product owner who defines and prioritizes product requirements.

Tester: Represents the role of a quality assurance tester who performs testing and ensures the quality of the software.

These roles serve as initial options for assigning roles to team members. The application allows creating additional
roles as needed to suit the specific requirements of the organization.

### How to run:

To run the application, follow these steps:

- Ensure you have docker and docker-compose on your machine.
- Clone the project repository from GitHub.
- run the command in the project root folder:

```cmd
docker-compose up --build
```

### Endpoints:

- Spring-Boot-Admin UI: http://localhost:8080/
- Swagger-UI: http://localhost:8080/swagger-ui.html
- Zipkin: http://localhost:9411/

## Walkthrough

This walkthrough guide will take you through the process of creating a membership and viewing it using the Swagger
interface. Additionally, it will describe how to access the Zipkin interface and visualize the tracing of the API calls.

## Prerequisites

Before starting the walkthrough, ensure that you have the following prerequisites:

- The Roles App project is set up and running.
- Swagger-UI is accessible at `http://localhost:8080/swagger-ui.html`.
- Zipkin is running and accessible at `http://localhost:9411`.

## Creating a Membership

To create a membership using the Swagger interface, follow these steps:

1. Open your web browser and navigate to `http://localhost:8080/swagger-ui.html`.

2. Locate the "membership-controller" section and expand it.

3. Click on the "POST /memberships" endpoint to expand it.

4. Click on the "Try it out" button.

5. In the "Request body" section, paste the following payload:

   ```json
   {
     "teamId": "7676a4bf-adfe-415c-941b-1739af07039b",
     "memberId": "371d2ee8-cdf4-48cf-9ddb-04798b79ad9e",
     "roleId": "a8cf155d-8b7e-4c34-b559-5e6235296710"
   }

6. Click on the "Execute" button to send the request.

## View the Membership

To view the created membership using the Swagger interface, follow these steps:

1. Open a web browser and go to http://localhost:8080/swagger-ui.html.
2. Locate the "membership-controller" section and expand it.
3. Click on the "GET /memberships" endpoint to expand it.
4. Click on the "Try it out" button and if add some attribute values to filer if you want.
5. Click on the "Execute" button to send the request.
6. The response will display the details of the memberships.

## Access the Zipkin Interface and View Tracing

To access the Zipkin interface and view tracing of API calls, follow these steps:

1. Open a web browser and go to http://localhost:9411.
2. The Zipkin interface will open, showing a search form.
3. By default, the "Find Traces" form will display recent traces.
4. To search for traces related to the Roles App, enter the service name (e.g., "roles-app") in the "Service Name"
   field.
5. Click on the "Find Traces" button to search for traces.
6. The interface will display the traced requests, showing the duration, service name, and other details of the traces.

## What to improve in the future

### Enhance Security:

Consider implementing a robust authentication and authorization mechanism using Spring Security. This will provide more
secure access control to the application's endpoints and resources.

### Enhance Integration Tests:

Although the project includes unit tests and some integration tests, it is recommended to expand the test coverage by
adding more comprehensive integration tests. These tests should cover various scenarios and interactions between
different components of the system.

### Implement Continuous Integration and Deployment (CI/CD):

Set up a CI/CD pipeline to automate the build, testing, and deployment processes. This will help streamline development,
ensure code quality, and enable faster and more reliable deployments.

### Optimize Database Queries:

Analyze the performance of database queries and consider optimizing them for better efficiency. Identify slow-running
queries and optimize them using techniques like indexing, query tuning, or caching.

### Refactor and Simplify Code:

Regularly review the codebase and identify areas where refactoring and simplification can be done. This includes
removing duplicate code, improving code readability, and applying design patterns or best practices to enhance the
overall code quality considering a team decision.

### Setup application for a real cloud/infrastructure

Prepare the application for real infrastructure or services. For example, using a Redis cache instead the in memory
cache from Spring Framework.

### Add the missing validations on some specific business flows

Given the lack of time, I could just add a few validations for consistency. So there are some external validations
missing like in PUT and PATCH membership endpoints