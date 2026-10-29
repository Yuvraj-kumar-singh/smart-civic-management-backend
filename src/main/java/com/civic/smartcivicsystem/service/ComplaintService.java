package com.civic.smartcivicsystem.service;

import com.civic.smartcivicsystem.entity.Complaint;
import com.civic.smartcivicsystem.entity.User;
import com.civic.smartcivicsystem.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserService userService;

    public Complaint registerComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }

    public List<Complaint> getComplaintsByStatus(Complaint.Status status) {
        return complaintRepository.findByStatus(status);
    }

    public List<Complaint> getComplaintsByUserId(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

    // Status sirf wahi OFFICER badal sakta hai jisko complaint assign hui hai.
    // Admin ka kaam sirf assign karna hai.
    public String updateComplaintStatus(Long complaintId, Complaint.Status newStatus, Long requestingUserId) {
        User requestingUser = userService.getUserById(requestingUserId);

        if (requestingUser == null) {
            return "User not found!";
        }

        if (requestingUser.getRole() != User.Role.OFFICER) {
            return "Permission Denied! Only the assigned officer can update the status.";
        }

        Complaint existingComplaint = complaintRepository.findById(complaintId).orElse(null);
        if (existingComplaint == null) {
            return "Complaint not found!";
        }

        boolean isAssignedToHim = existingComplaint.getAssignedTo() != null
                && existingComplaint.getAssignedTo().getId().equals(requestingUser.getId());
        if (!isAssignedToHim) {
            return "Permission Denied! This complaint is not assigned to you.";
        }

        existingComplaint.setStatus(newStatus);
        complaintRepository.save(existingComplaint);
        return "Status updated successfully to " + newStatus;
    }

    public String assignComplaintToStaff(Long complaintId, Long staffId, Long requestingUserId) {
        User requestingUser = userService.getUserById(requestingUserId);

        if (requestingUser == null || requestingUser.getRole() != User.Role.ADMIN) {
            return "Permission Denied! Only Admin can assign complaints.";
        }

        User officer = userService.getUserById(staffId);
        if (officer == null || officer.getRole() != User.Role.OFFICER) {
            return "Invalid Officer!";
        }

        Complaint existingComplaint = complaintRepository.findById(complaintId).orElse(null);
        if (existingComplaint == null) {
            return "Complaint not found!";
        }

        existingComplaint.setAssignedTo(officer);
        existingComplaint.setStatus(Complaint.Status.IN_PROGRESS);
        complaintRepository.save(existingComplaint);
        return "Complaint assigned successfully to " + officer.getName();
    }

    public List<Complaint> getComplaintsAssignedToStaff(Long staffId) {
        return complaintRepository.findByAssignedToId(staffId);
    }
}