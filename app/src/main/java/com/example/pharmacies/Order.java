package com.example.pharmacies;

public class Order {
    private int number;
    private int user_id;
    private User user;
    private String orderDate;
    private String orderTime;
    private double orderAmount;
    private int orderPoints;
    private Pharmacy pharmacy;

    // Additional fields
    private String deliveryDate;
    private String deliveryTime;
    private String deliveryName;
    private String deliveryNumber;
    private String status;
    private String order_detail;

    // Constructor with required fields

    public Order(){
      user= new User();
      pharmacy= new Pharmacy();

    }
    public Order(String orderDate, String orderTime, double orderAmount) {
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderAmount = orderAmount;
        this.orderPoints = (int) (0.2 * orderAmount); // Automatically calculate order points
    }

    // Getters and setters for required fields
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getOrderPoints() {
        return orderPoints;
    }

    public void setOrderPoints(int orderPoints) {
        this.orderPoints = orderPoints;
    }

    // Getters and setters for additional fields
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(String order_detail) {
        this.order_detail = order_detail;
    }
}
