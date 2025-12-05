package com.member.gym.controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.member.gym.model.Member;

@Controller
public class MemberController {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    

public Connection getConnection() {
    Connection connection = null;
    try {
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        System.out.println("Connection established successfully");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return connection;
}
@GetMapping("/")
public String home(Model model) {
    List<Member> members = getAllMembers();
    model.addAttribute("members", members);
    return "index";
}
private List<Member> getAllMembers() {
    List<Member> members = new ArrayList<>();
    try (Connection connection = getConnection()) {
        String sql = "SELECT * FROM members";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Member member=new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setEmail(resultSet.getString("email"));
                member.setPhone(resultSet.getString("phone"));
                member.setMembership_type_id(resultSet.getString("membership_type_id"));
                member.setJoining_date(resultSet.getDate("joining_date").toLocalDate());
                member.setStatus(resultSet.getString("status"));
                members.add(member);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return members;
}
@GetMapping("/add")
public String showAddMemberForm(Model model) {
    model.addAttribute("member", new Member());
    return "add-member";
}
@PostMapping("/add")
public String addMember(@ModelAttribute Member member, Model model){
    try (Connection connection = getConnection()){
        String sql = "INSERT INTO members (id, name, email, phone, membership_type_id, joining_date, status) VALUES (?,?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,member.getId());
        statement.setString(2, member.getName());
        statement.setString(3, member.getEmail());
        statement.setString(4, member.getPhone());
        statement.setString(5, member.getMembership_type_id());
        LocalDate localDate=member.getJoining_date();
        if (localDate!=null){
        statement.setDate(6,java.sql.Date.valueOf(localDate));}
        else{
            statement.setNull(6, java.sql.Types.DATE);
        }
        statement.setString(7, member.getStatus());
        statement.executeUpdate();
        model.addAttribute("success", "Successfully added member " + member.getName());
    } 
     catch (SQLException e) {
        model.addAttribute("error", "Error adding member " + e.getMessage());
        e.printStackTrace();
    }
    return "add-member";
    }
@GetMapping("/search")
public String searchMembers(@RequestParam String name, Model model) {
    List<Member> members = new ArrayList<>();
    try (Connection conn = getConnection()) {
        String sql = "SELECT * FROM members WHERE name LIKE ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "%" + name + "%");
        statement.executeQuery();
        ResultSet resultSet = statement.getResultSet();
        // Iterate over the result set and create a list of employees
        while (resultSet.next()) {
            Member  member= new Member();
            member.setId(resultSet.getInt("id"));
            member.setName(resultSet.getString("name"));
            member.setEmail(resultSet.getString("email"));
            member.setPhone(resultSet.getString("phone"));
            member.setMembership_type_id(resultSet.getString("membership_type_id"));
            java.sql.Date sqlDate=resultSet.getDate("joining_date");
            if(sqlDate!=null){
                member.setJoining_date(sqlDate.toLocalDate());
            }
            else{
                member.setJoining_date(null);
            }
            member.setStatus(resultSet.getString("status"));

            members.add(member);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    model.addAttribute("members", members);
    return "index";
}


@GetMapping("/edit/{id}")
public String showEditForm(@PathVariable int id, Model model) {
    try {
        Member member = getMemberById(id);
        model.addAttribute("member", member);
        return "edit-member";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "index";
    }
}
private Member getMemberById(int id) throws Exception {
    try (Connection conn = getConnection()) {
        String sql = "SELECT * FROM members WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Member member = new Member();
            member.setId(resultSet.getInt("id"));
            member.setName(resultSet.getString("name"));
            member.setEmail(resultSet.getString("email"));
            member.setPhone(resultSet.getString("phone"));
            member.setMembership_type_id(resultSet.getString("membership_type_id"));
            java.sql.Date sqlDate=resultSet.getDate("joining_date");
            if(sqlDate!=null){
                member.setJoining_date(sqlDate.toLocalDate());
            }
            else{
                member.setJoining_date(null);
            }
        
           member.setStatus(resultSet.getString("status"));
            return member;
        } else {
            throw new Exception("Member not found");
        }
    }
}

@PostMapping("/edit/{id}")
public String editMember(@PathVariable int id, @ModelAttribute Member member, Model model) {
    try (Connection conn = getConnection()) {
        String sql = "UPDATE members SET name=?, email=?, phone=?, membership_type_id=?, joining_date=?, status=? WHERE id=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, member.getName());
        statement.setString(2, member.getEmail());
        statement.setString(3, member.getPhone());
        statement.setString(4, member.getMembership_type_id());
        LocalDate localDate=member.getJoining_date();
        if (localDate!=null){
        statement.setDate(5,java.sql.Date.valueOf(localDate));}
        else{
            statement.setNull(5, java.sql.Types.DATE);
        }
        statement.setString(6, member.getStatus());
        statement.setInt(7,id);
        statement.executeUpdate();
        model.addAttribute("success","Sucessfully updated member "+member.getName());
        return "edit-member";

    } catch (SQLException e) {
        model.addAttribute("error", "Error updating member: " + e.getMessage());
        return "edit-member";
    }
}
@GetMapping("/delete/{id}")
public String showDeleteForm(@PathVariable int id, Model model) {
    try {
        Member member = getMemberById(id);
        model.addAttribute("member", member);
        return "delete-member";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "index";
    }
}

@PostMapping("/delete/{id}")
public String deleteMember(@PathVariable int id,Model model, RedirectAttributes redirectAttributes) {
    try (Connection conn = getConnection()) {
        String sql = "DELETE FROM members WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        redirectAttributes.addFlashAttribute("success", "Member with ID " +id+ "is successfully deleted");
        return "redirect:/";
    }catch(SQLException e){
    model.addAttribute("error", "error deleted member" +e.getMessage());
    e.printStackTrace();
    return "redirect:/";
    }
}}


