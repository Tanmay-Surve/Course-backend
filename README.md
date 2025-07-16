📚 Course Search API with Elasticsearch
A beginner-friendly Spring Boot application that helps you search and filter courses with powerful Elasticsearch features.

🚀 Features
✅ Search Courses - Find courses by title, description, or keywords
✅ Filters - Narrow down by category, age range, price, and more
✅ Sorting - Sort by price (low-high/high-low) or upcoming sessions
✅ Pagination - Browse results page by page
✅ Autocomplete (Bonus) - Get instant suggestions as you type
✅ Fuzzy Search (Bonus) - Find results even with typos

⚙️ Prerequisites
Before running the project, make sure you have:
Java 17+ (Recommended: OpenJDK)
Docker (For Elasticsearch) - Install Docker
Maven (For building the project)

🛠 Setup & Run
1️⃣ Start Elasticsearch (Using Docker)
Open a terminal and run: docker-compose up -d

✅ Verify Elasticsearch is running: curl http://localhost:9200
(You should see a JSON response with "You Know, for Search")

2️⃣ Run the Spring Boot Application
Build the project: ./mvnw clean install
Start the application: ./mvnw spring-boot:run
📌 Server will start at: http://localhost:8080

🔍 How to Use the API
1️⃣ Search Courses (GET /api/search)
📌 Example API Requests
1. Basic Search: curl "http://localhost:8080/api/search?q=math"
2. Filter by Category & Price: curl "http://localhost:8080/api/search?category=Science&minPrice=100&maxPrice=500"
3. Sort by Price (Low to High): curl "http://localhost:8080/api/search?sort=priceAsc"
4. Upcoming Courses Only: curl "http://localhost:8080/api/search?startDate=2025-07-01"

🎯 Bonus Features
1️⃣ Autocomplete Suggestions (GET /api/search/suggest)
📌 Example API Requests
Basic Search: curl "http://localhost:8080/api/search/suggest?q=phy"

🙌 Thank You!
