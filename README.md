# Appointment Scheduler
A JavaFX application for appointment scheduling.

## Build Requirements
- Java 11
- JavaFX 11
- Maven

## How to run
The entryway into the program is app.App.java, which presents a main() function to launch the JavaFX app.  This is specified in the Maven configuration in pom.xml.

## Design Patterns
The application use the MVC pattern for the domain layers and the Repository pattern for the data layers, with an institial service layer that maps data flows.

### Presentation Layer
- **`model/`** - implements the domain entities
- **`resources/views/`** - contains the JavaFX user interface files
- **`controller/`** - responsible for interactions between UI views and domain objects

### Service Layer
- **`service/`** - bidirectional translation of Models (domain objects) to DTOs for transfer to Data layer

### Data Layer
- **`dto/`** - contains DTOs (Data Transfer Object) for transfering data between Presentation and Data layers
- **`dal/`** - implements DAOs (Data Access Object) to abstract the process of persisting data to an arbitrary database
