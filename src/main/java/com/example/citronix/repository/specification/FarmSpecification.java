package com.example.citronix.repository.specification;

import com.example.citronix.domain.dtos.request.farm.FarmSearchCriteriaDto;
import com.example.citronix.domain.entity.Farm;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class FarmSpecification {
    public static Specification<Farm> getFarmByCriteria(FarmSearchCriteriaDto criteria) {

        // TODO SEARCH THE FARM
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + criteria.getLocation().toLowerCase() + "%"
                ));
            }

            if (criteria.getArea() != null) {
                predicates.add(criteriaBuilder.equal(root.get("area"), criteria.getArea()));
            }

            if (criteria.getCreationDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("creationDate"), criteria.getCreationDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
