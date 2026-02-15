package com.lowkkid.github.customersupport.domain.repository;

import com.lowkkid.github.customersupport.domain.projection.CategoryCount;
import com.lowkkid.github.customersupport.domain.projection.PriorityCount;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.domain.entity.SupportMessage;
import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.domain.entity.enums.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {

    @Query("""
    SELECT new com.lowkkid.github.customersupport.dto.SupportMessageDto(
        sm.id, sm.customerName, sm.messageBody, sm.category, sm.priority, sm.status, sm.createdAt
    )
    FROM SupportMessage sm
    WHERE (:priority IS NULL OR sm.priority = :priority)
            AND (:category IS NULL OR sm.category = :category)
            AND (:status IS NULL OR sm.status = :status)
           \s""")
    Page<SupportMessageDto> findAll(Priority priority, Category category, Status status, Pageable pageable);

    @Query("SELECT sm.category AS category, COUNT(sm) AS count FROM SupportMessage sm WHERE sm.status = 'UNRESOLVED' GROUP BY sm.category")
    List<CategoryCount> countUnresolvedByCategory();

    @Query("SELECT sm.priority AS priority, COUNT(sm) AS count FROM SupportMessage sm WHERE sm.status = 'UNRESOLVED' GROUP BY sm.priority")
    List<PriorityCount> countUnresolvedByPriority();

}
