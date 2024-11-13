# Inventory Management System (Spring Boot + Kafka)  

### **Overview**  
This project is a beginner-friendly Proof of Concept (POC) for an **Inventory Management System**.

It introduces core **Spring Boot** concepts like creating RESTful APIs, integrating a relational database, building microservices, and asynchronous communication via Kafka.  

By working on this project, you'll understand how to structure scalable systems, implement business logic, and design better solutions before writing code.  

---

### **Features**  
- **Microservices**: Independent services for Order and Product management.  
- **RESTful APIs**: Simple and efficient communication between services.  
- **Database Integration**: Use Spring Data JPA to interact with a relational database.  
- **Kafka Messaging**: Asynchronous communication between microservices.  
- **Design-First Approach**: Build sequence diagrams to plan the system flow.  

---

### **System Workflow**  
1. **Order Placement**: Users place orders with multiple products.  
2. **Availability Check**: Each product's stock is checked with the Product Service.  
3. **Event-Driven Updates**: The system publishes events (Order Placed, Out of Stock) using Kafka.  
4. **Inventory Updates**: Product Service updates stock levels asynchronously.  
5. **Order Status Update**: Real-time updates provided to users.  

---

### **Key Components**  

#### 1. **Order Service**  
- Handles incoming order requests.  
- Adjusts orders if products are unavailable.  
- Publishes events to Kafka on successful or failed order placement.  

#### 2. **Product Service**  
- Manages inventory data and updates stock levels.  
- Listens to Kafka events to handle inventory changes.  

#### 3. **Kafka Integration**  
- Ensures reliable communication between services.  
- Enables scalability and resilience through asynchronous messaging.  

---

### **Diagrams**

![image](https://github.com/user-attachments/assets/8edbb125-aea5-45e7-b6ed-2d4feef9167b)



![image](https://github.com/user-attachments/assets/8a199895-f7cf-4435-baec-652148fbfb6d)



### **Project Setup**  

#### **Prerequisites**  
- Java 8 or later  
- Spring Boot (2.x or later)  
- MySQL (or any other relational database)  
- Apache Kafka  

#### **Steps to Run the Project**  
1. Clone the repository.  
2. Set up MySQL and update database configurations in `application.properties`.  
3. Start Kafka broker and zookeeper.  
4. Build and run the microservices (Order Service and Product Service).  
5. Test the APIs using tools like Postman or cURL.  

---

### **Technologies Used**  
- **Backend**: Spring Boot, Spring Data JPA, Kafka  
- **Database**: MySQL  
- **Messaging**: Apache Kafka  
- **Tools**: Postman (API testing), Docker (optional)  

---

### **Projects Goals**  
- **Understand Microservices**: Learn how to create and run independent services.  
- **Explore Kafka**: Dive into event-driven architecture.  
- **Learn Design Skills**: Create sequence diagrams to visualize system flows.  
- **Develop Real-World Systems**: Gain hands-on experience in building scalable and robust systems.  

---

### **How to Contribute**  
1. Fork the repository.  
2. Work on your feature branch.  
3. Submit a pull request for review.  

  

---

### **Additional Learning Projects**  
Once you've completed this, consider exploring:  
- Setting up CI/CD for Spring Boot using Jenkins or GitHub Actions.  
- Adding file upload/download features to your Spring Boot app.  
- Writing and running tests with JUnit and Mockito.  

---


Happy coding! 🚀
