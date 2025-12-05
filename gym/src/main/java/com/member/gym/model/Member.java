package com.member.gym.model;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String membership_type_id;
    @DateTimeFormat(pattern="yyyy-MM-dd")

    private LocalDate joining_date;
    private String status;

    // Constructors
    public Member() {}
    
    public Member(int id, String name, String email, String phone, String membership_type_id,LocalDate joining_date, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membership_type_id = membership_type_id;
        this.joining_date = joining_date;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMembership_type_id() { return membership_type_id; }
    public void setMembership_type_id(String membership_type_id) { this.membership_type_id = membership_type_id; }
    public LocalDate getJoining_date() { return joining_date; }
    public void setJoining_date(LocalDate joining_date) { this.joining_date = joining_date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
    

