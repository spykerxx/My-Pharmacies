# 💊 My Pharmacies

My Pharmacies is a comprehensive Android application designed to connect customers, pharmacies, and delivery personnel in a seamless platform for browsing pharmacies, purchasing products, managing orders, and communicating in real-time.  

This project was developed as a full-stack app using **Java** for Android, with a **MySQL** backend accessed via custom **PHP APIs**. It was built primarily for learning purposes and showcases robust multi-actor workflows, clean architecture, and a smooth, polished UI.

---

## 🚀 Overview

The app supports three types of users with distinct roles and functionalities:

### 1. Customers
- Browse pharmacies and products
- Compare products and check offers/discounts
- Manage profiles including personal details, address, medical history, and profile image
- Live chat with doctors and pharmacies
- Place orders and add multiple payment methods
- Track order status and delivery in real-time with map integration
- Search, add to favorites, remove items, and more

### 2. Pharmacies
- Add, modify, and delete products with images and detailed info
- Manage pharmacy profile with address and contact details
- Receive and accept/reject orders from customers
- Respond to chat requests
- View order history and customer interactions

### 3. Delivery Personnel
- View available pending orders to accept or reject
- Manage personal details including vehicle info (bike/car)
- Real-time order tracking and delivery location sharing visible to both customers and pharmacies

---

## 🛠 Tech Stack

- Android app developed in **Java** using **Android Studio**
- **MySQL** relational database with normalized tables for data integrity
- Custom **PHP APIs** for server-side business logic and database interaction
- Localhost **XAMPP** server for backend hosting (for learning/demo purposes)
- Real-time chat and order status updates implemented within app architecture
- Google Maps API integration for delivery tracking
- Clean and smooth UI design emphasizing user experience

---

## ⚠️ Important Notes

- This app uses a **local server (localhost)** for backend services, powered by **XAMPP**. To run the app successfully, the backend PHP APIs and MySQL database must be hosted locally.
- It is designed as a **learning and demonstration project** and not deployed on a live server.
- Deployment to a production environment would require server migration and configuration.

---

## 📂 Project Structure Highlights

- Multi-user role management with separate UI flows for customers, pharmacies, and delivery personnel
- Efficient REST API integration for CRUD operations on products, orders, and profiles
- Real-time synchronization of orders and chat messages
- Use of maps and location services for live delivery tracking

---

## 📸 Screenshots

_ Included within the project files (check screenshots folder) _

---

## 🚀 Running the App

1. Set up **XAMPP** and start Apache and MySQL services.
2. Import the provided **MySQL database dump** into your local MySQL server.
3. Place the PHP API files in the `htdocs` folder of XAMPP.
4. Configure the PHP files with your local database credentials.
5. Open the project in **Android Studio**.
6. Update the API base URL in the app to point to `http://localhost/...` as needed.
7. Build and run the app on an emulator or physical device connected to the same network.

---

## 👤 Author

Mohammed Ghabayen  
mohammedgh30@gmail.com | https://www.linkedin.com/in/mohammed-gh-064943142/

---

## 📚 Additional Notes

This project highlights end-to-end Android development skills including:

- Frontend UI/UX design in Java with Android Studio
- Backend development with PHP and MySQL
- API design and integration
- Multi-role user management
- Real-time features like chat and order tracking

It demonstrates ability to build scalable, feature-rich applications from scratch.

---
