# 📚 Course Search API (Spring Boot + Elasticsearch)

A Spring Boot application that indexes and searches course data using Elasticsearch. It supports full-text search, filters, sorting, pagination, autocomplete, and fuzzy matching.

---

## 🚀 Features

✅ Bulk index 50+ course documents  
✅ Full-text search on title and description  
✅ Filters: age, category, type, price, session date  
✅ Sorting: upcoming session, price (asc/desc)  
✅ Pagination  
✅ Autocomplete suggestions *(Bonus)* 

---

## 📦 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data Elasticsearch
- Elasticsearch 7.17
- Docker + Docker Compose
- Maven

---

## 🗂️ Project Structure

com.tanmay.courseapp
├── config/ # Elasticsearch settings and DataLoader
├── controller/ # REST APIs for search and suggestions
├── document/ # CourseDocument for indexing
├── dto/ # Request/response DTOs
├── repository/ # Spring Data Elasticsearch interface
├── service/ # Business logic for querying ES
└── resources/
├── application.yml
├── elasticsearch-settings.json
├── course-mappings.json
└── sample-courses.json

---

## 🐳 1. Start Elasticsearch via Docker

### ✅ Prerequisite
- Install [Docker](https://docs.docker.com/get-docker/)

### ▶️ Run Elasticsearch

In the project root, run:

docker-compose up -d
This starts a single-node Elasticsearch cluster at:
http://localhost:9200
You should see a JSON response with "You Know, for Search".

⚙️ 2. Run the Spring Boot App
🔨 Build the application
./mvnw clean install

🚀 Start the application
./mvnw spring-boot:run
The server will start at:
http://localhost:8080

🔍 3. Usage – Search API

GET /api/search
Query Parameters
Param	Description
q	Keyword (searches title + description)
minAge	Minimum age filter
maxAge	Maximum age filter
category	Filter by course category
type	Filter by type (ONE_TIME, COURSE, CLUB)
minPrice	Minimum price
maxPrice	Maximum price
startDate	ISO-8601 start date filter
sort	Sort by upcoming, priceAsc, priceDesc
page	Page number (default: 0)
size	Page size (default: 10)


# Basic full-text search + age + price sorting
curl "http://localhost:8080/api/search?q=math&minAge=6&sort=priceAsc&page=0&size=5"

# Filter by category and type
curl "http://localhost:8080/api/search?category=Science&type=COURSE"

# Range price filter
curl "http://localhost:8080/api/search?minPrice=200&maxPrice=500"

# Upcoming courses only
curl "http://localhost:8080/api/search?startDate=2025-07-10"


💡 4. Autocomplete (Bonus)
curl "http://localhost:8080/api/search/suggest?q=phy"

🙌 Thank You!
