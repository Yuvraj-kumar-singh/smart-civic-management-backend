package com.civic.smartcivicsystem.repository;

import com.civic.smartcivicsystem.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStatus(Complaint.Status status);

    List<Complaint> findByUserId(Long userId);

    List<Complaint> findByAssignedToId(Long assignedToId);
}