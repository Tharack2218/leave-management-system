
# 📌 Leave Management System

A full-stack web application that enables employees to apply for leave and allows administrators to manage, approve, or reject leave requests efficiently.

---

## 🚀 Features

### 👨‍💼 Employee

* Apply for leave using start and end dates
* Automatic calculation of total leave days
* View leave history
* Track leave status (Pending / Approved / Rejected)

---

### 🧑‍💻 Admin

* View all leave requests
* Approve or reject leave applications
* Manage user access (approve new users)

---

### 🔐 Access Control

* New users must be **approved by admin** before accessing the system
* Role-based functionality (Admin / Employee)

---

## 🏗️ Tech Stack

### Frontend

* React.js
* HTML, CSS, JavaScript

### Backend

* Spring Boot (Java)
* REST APIs

### Database

* MySQL (XAMPP)

---

## 📂 Project Structure

```
leave-management-system/
│
├── Backend/
│   └── Leave-project/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│
├── Frontend/
│   └── leave-frontend/
│       ├── src/
│       ├── public/
│
├── WEBSITE IMAGES/
│
└── README.md
```

---

## 🔧 Installation & Setup

### 1️⃣ Clone Repository

```
git clone https://github.com/Tharack2218/leave-management-system.git
cd leave-management-system
```

---

### 2️⃣ Backend Setup (Spring Boot)

```
cd Backend/Leave-project
```

Update `application.yml`:

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leave_db
    username: root
    password:
  jpa:
    hibernate:
      ddl-auto: update
server:
  port: 8089
```

Run backend:

```
mvn spring-boot:run
```

---

### 3️⃣ Frontend Setup (React)

```
cd Frontend/leave-frontend
npm install
npm start
```

---

## 🔗 API Endpoints

### 🔐 Authentication

* `POST /api/auth/register`
* `POST /api/auth/login`
* `PUT /api/auth/approve/{id}`

---

### 📄 Leave Management

* `POST /api/leaves/apply`
* `GET /api/leaves/user/{id}`
* `GET /api/leaves`
* `PUT /api/leaves/{id}/approve`
* `PUT /api/leaves/{id}/reject`

---

## 📸 Screenshots available in github

## 🎥 Demo Video

👉 [[https://drive.google.com/your-video-link](https://drive.google.com/your-video-link](https://drive.google.com/file/d/1dXj_d6iPdaYvWUcSzpsHHsIDYr70ZY3q/view?usp=sharing))

---

## 🧠 Future Enhancements

* ⏰ 12-hour leave restriction
* 📅 1-day advance leave limit
* 🔐 JWT authentication
* 📧 Email notifications
* 📱 Mobile responsive UI

---

## 🐞 Common Issues

* 404 Error → Check API URL
* CORS Error → Enable `@CrossOrigin`
* Database not connecting → Ensure MySQL is running

---

## 🙌 Author

**Ramesh**
CSE Graduate | Full Stack Developer
Tech Stack: Java | Spring Boot | React

---

## ⭐ Note

This project demonstrates a real-world leave approval workflow with role-based access and full-stack integration.
