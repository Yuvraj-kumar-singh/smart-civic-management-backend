package com.civic.smartcivicsystem.controller;

import com.civic.smartcivicsystem.entity.Complaint;
import com.civic.smartcivicsystem.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintservice;

    @PostMapping("/register")
    public Complaint registerComplaint(@RequestBody Complaint complaint) {
        return complaintservice.registerComplaint(complaint);
    }

    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintservice.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable Long id) {
        return complaintservice.getComplaintById(id);
    }

    @GetMapping("/status/{status}")
    public List<Complaint> getComplaintsByStatus(@PathVariable Complaint.Status status) {
        return complaintservice.getComplaintsByStatus(status);
    }

    @GetMapping("/user/{userId}")
    public List<Complaint> getComplaintsByUserId(@PathVariable Long userId) {
        return complaintservice.getComplaintsByUserId(userId);
    }

    @PutMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam Complaint.Status newStatus,
            @RequestParam Long userId) {
        return complaintservice.updateComplaintStatus(id, newStatus, userId);
    }

    // Naya endpoint — Admin complaint ko OFFICER ko assign karega
    // PUT request: http://localhost:8080/api/complaints/1/assign?staffId=4&userId=3
    @PutMapping("/{id}/assign")
    public String assignComplaint(
            @PathVariable Long id,
            @RequestParam Long staffId,
            @RequestParam Long userId) {
        return complaintservice.assignComplaintToStaff(id, staffId, userId);
    }

    // Naya endpoint — OFFICER apni assigned complaints dekhega
    // GET request: http://localhost:8080/api/complaints/assigned/4
    @GetMapping("/assigned/{staffId}")
    public List<Complaint> getAssignedComplaints(@PathVariable Long staffId) {
        return complaintservice.getComplaintsAssignedToStaff(staffId);
    }
}