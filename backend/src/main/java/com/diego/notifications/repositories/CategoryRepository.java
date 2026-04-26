package com.diego.notifications.repositories;

import com.diego.notifications.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing {@link com.diego.notifications.models.Category} entities.
 *
 * <p>Provides data access operations for the category catalog, which defines
 * the classification of messages within the system. Categories are expected
 * to be preloaded via database seeders and accessed primarily by their unique code.</p>
 *
 * <p>This repository supports lookup operations required during message creation
 * and validation processes.</p>
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCode(String code);
}
