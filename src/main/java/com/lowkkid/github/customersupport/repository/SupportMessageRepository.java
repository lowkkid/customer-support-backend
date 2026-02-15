package com.lowkkid.github.customersupport.repository;

import com.lowkkid.github.customersupport.model.SupportMessage;
import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {

    List<SupportMessage> findByCategory(Category category);

    List<SupportMessage> findByPriority(Priority priority);

    List<SupportMessage> findByCategoryAndPriority(Category category, Priority priority);

    List<SupportMessage> findByResolved(boolean resolved);

    long countByCategory(Category category);

    long countByPriority(Priority priority);
}
