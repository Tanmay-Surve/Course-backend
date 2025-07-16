# Spring Boot Elasticsearch Course Search Application

This project is a Spring Boot application that indexes course data into Elasticsearch and provides REST endpoints for searching courses with filtering, sorting, pagination, and optional autocomplete and fuzzy search features. Below are the instructions to set up, run, and test the application.

## Prerequisites
- Java 17 or later
- Maven 3.8.x or later
- Docker and Docker Compose
- curl or any HTTP client (e.g., Postman) for testing

## Project Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.coursesearch
│   │   │       ├── config        # Elasticsearch and application configurations
│   │   │       ├── controller    # REST controllers
│   │   │       ├── document      # Course document model
│   │   │       ├── repository    # Elasticsearch repository
│   │   │       ├── service       # Business logic
│   │   └── resources
│   │       ├── sample-courses.json  # Sample course data
│   │       └── application.yml      # Application configuration
│   └── test                         # Unit and integration tests
├── docker-compose.yml               # Elasticsearch Docker setup
└── README.md                        # This file
```

## Setup Instructions

### 1. Launch Elasticsearch
1. Ensure Docker is running.
2. Start a single-node Elasticsearch cluster using Docker Compose:
   ```bash
   docker-compose up -d
   ```
3. Verify Elasticsearch is running:
   ```bash
   curl http://localhost:9200
   ```
   Expected output:
   ```json
   {
     "name": "...",
     "cluster_name": "docker-cluster",
     "version": { "number": "8.11.0", ... },
     ...
   }
   ```

### 2. Build and Run the Application
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
4. The application will start on `http://localhost:8080`.

### 3. Data Ingestion
- The application automatically loads `sample-courses.json` from `src/main/resources` on startup and bulk-indexes the data into the `courses` index in Elasticsearch.
- To verify data ingestion, check the logs for a message like:
  ```
  Successfully indexed 50 courses to Elasticsearch
  ```
- Alternatively, query the index:
  ```bash
  curl http://localhost:9200/courses/_count
  ```

## API Endpoints

### 1. Search Courses (Assignment A)
**Endpoint**: `GET /api/search`

**Query Parameters**:
- `q`: Search keyword for title and description (optional)
- `minAge`, `maxAge`: Age range filter (optional, numeric)
- `category`: Exact category filter (e.g., "Math", "Science") (optional)
- `type`: Exact type filter (e.g., "ONE_TIME", "COURSE", "CLUB") (optional)
- `minPrice`, `maxPrice`: Price range filter (optional, decimal)
- `startDate`: Filter courses on or after this ISO-8601 date (e.g., "2025-06-01T00:00:00Z") (optional)
- `sort`: Sort order (`upcoming`, `priceAsc`, `priceDesc`) (default: `upcoming`)
- `page`: Page number (default: 0)
- `size`: Results per page (default: 10)

**Example Requests**:
1. Search for "Physics" courses in the "Science" category, sorted by price ascending:
   ```bash
   curl "http://localhost:8080/api/search?q=Physics&category=Science&sort=priceAsc"
   ```
   Expected Response:
   ```json
   {
     "total": 5,
     "courses": [
       { "id": "1", "title": "Physics 101", "category": "Science", "price": 50.0, "nextSessionDate": "2025-06-10T15:00:00Z" },
       ...
     ]
   }
   ```

2. Search courses for ages 10–14, starting after June 1, 2025:
   ```bash
   curl "http://localhost:8080/api/search?minAge=10&maxAge=14&startDate=2025-06-01T00:00:00Z"
   ```

### 2. Autocomplete Suggestions (Assignment B - Bonus)
**Endpoint**: `GET /api/search/suggest`

**Query Parameters**:
- `q`: Partial title for autocomplete suggestions (required)

**Example Request**:
```bash
curl "http://localhost:8080/api/search/suggest?q=phy"
```
Expected Response:
```json
[
  "Physics 101",
  "Physical Chemistry Basics",
  ...
]
```

### 3. Fuzzy Search (Assignment B - Bonus)
- Fuzzy matching is enabled on the `title` field in the `/api/search` endpoint.
- Example: Search for "dinors" to match "Dinosaurs 101":
  ```bash
  curl "http://localhost:8080/api/search?q=dinors"
  ```
  Expected Response:
  ```json
  {
    "total": 1,
    "courses": [
      { "id": "10", "title": "Dinosaurs 101", "category": "Science", "price": 75.0, "nextSessionDate": "2025-07-01T10:00:00Z" }
    ]
  }
  ```

## Configuration
- Application properties are defined in `src/main/resources/application.yml`:
  ```yaml
  spring:
    elasticsearch:
      uris: http://localhost:9200
  ```
- If Elasticsearch is running on a different host/port, update `application.yml` accordingly.

## Testing
- Unit tests are located in `src/test/java` and cover service layer logic.
- Integration tests use Testcontainers to spin up an ephemeral Elasticsearch instance:
  ```bash
  mvn test
  ```
- Example test cases verify:
  - Correct filtering by category and age range
  - Sorting by price and session date
  - Fuzzy matching for titles (if Assignment B is implemented)

## Notes
- The `sample-courses.json` file contains 50 varied course entries with fields: `id`, `title`, `description`, `category`, `type`, `gradeRange`, `minAge`, `maxAge`, `price`, and `nextSessionDate`.
- The application uses Spring Data Elasticsearch for simplicity, with Jackson for JSON parsing.
- For Assignment B, the `courses` index includes a `suggest` field with a `completion` mapping for autocomplete.

## Troubleshooting
- If Elasticsearch fails to start, ensure port 9200 is free and Docker has sufficient resources (at least 2GB RAM).
- Check application logs (`logs/app.log`) for indexing or query errors.
- For Testcontainers issues, ensure Docker is running and accessible.
