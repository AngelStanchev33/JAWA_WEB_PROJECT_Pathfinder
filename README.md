# Pathfinder - Route Sharing Application

Pathfinder is a Spring Boot application that allows users to share and discover hiking routes. The application provides functionality for uploading, viewing, and managing hiking routes with GPX file support.

## Features

- User registration and authentication
- Route management:
  - Upload new routes with GPX files
  - View route details
  - Browse available routes
  - Route categorization (PEDESTRIAN, BICYCLE, MOTORCYCLE, CAR)
- Route details include:
  - Route name and description
  - Level of difficulty
  - GPX file visualization
  - Route category
  - Author information

## Technical Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL Database
- Thymeleaf
- ModelMapper
- Bootstrap

## Getting Started

1. Clone the repository
2. Configure your database connection in `application.properties`
3. Run the application using Spring Boot
4. Access the application at `http://localhost:8080`

## Project Structure

- `src/main/java/bg/softuni/pathfinder/`
  - `config/` - Configuration classes
  - `controller/` - MVC Controllers
  - `model/` - Entity and DTO classes
  - `repository/` - JPA Repositories
  - `service/` - Business logic
  - `web/` - Web controllers and views

## Contributing

Feel free to submit issues and enhancement requests! 