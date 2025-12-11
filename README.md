# Blogsphere – Full-Stack Blogging Platform  
A production-style Java Spring Boot web application that enables users to create, manage, and interact with blog content.  
The project demonstrates clean architecture, modular design, and real-world features such as authentication, notifications, likes, comments, subscriptions, dashboards, and content management.

---

## 🚀 Features

### **User & Authentication**
- User registration, login, and profile management
- Role-based access control
- Global exception handling using `ControllerAdvice`

### **Blog Management**
- Create, update, delete blog posts
- Categorize posts using dynamic categories
- Rich DTO-based request/response architecture

### **Engagement Features**
- Comment on posts  
- Like/unlike functionality  
- Subscribe to other bloggers  
- Notification system for likes, comments, and follows  

### **Dashboard & Analytics**
- Personal dashboard showing:
  - Post performance  
  - Category distribution  
  - Engagement statistics (likes, comments, followers)

### **Clean & Scalable Architecture**
- MVC structure: Controllers → Services → Repositories  
- DTO mapping for clean API layers  
- Service-layer unit tests (CommentService, PostService, UserService)  
- Repository-layer JPA queries  

---

## 🛠️ Tech Stack

**Backend:**  
- Java 17  
- Spring Boot (Web, Security, JPA, Validation)  
- Spring MVC  
- Spring Data JPA  

**Frontend:**  
- Thymeleaf templates  
- HTML, CSS, Bootstrap  

**Database:**  
- H2 (for tests)  
- MySQL / configurable RDBMS  

**Build Tool:**  
- Maven  

**Testing:**  
- JUnit  
- Surefire test reports  

---

## 📂 Project Structure

Blogsphere/
│
├── src/main/java/org/example/blogsphere/
│ ├── controller/ # REST + UI controllers (Auth, Blog, Category, Dashboard, etc.)
│ ├── service/ # Business logic layer
│ ├── repository/ # JPA repositories
│ ├── dto/ # Request / Response objects
│ ├── entity/ # Database entities
│ ├── exception/ # Custom exceptions + handlers
│ ├── config/ # Security, CORS, global config
│ └── BlogSphereApplication.java
│
├── src/main/resources/
│ ├── templates/ # Thymeleaf pages
│ ├── static/ # CSS, JS, images
│ └── application.properties
│
├── target/ # Build output + test classes
│
├── Web Application Project Proposal.docx
└── README.md


---

## ▶️ How to Run the Application

### **1. Clone the repository**
```bash
git clone https://github.com/<your-username>/Blogsphere.git
cd Blogsphere
2. Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/blogsphere
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

3. Build & Run
mvn clean install
mvn spring-boot:run

4. Access in Browser
http://localhost:8080

🧪 Testing

Run unit tests:

mvn test


Surefire reports located at:

target/surefire-reports/
