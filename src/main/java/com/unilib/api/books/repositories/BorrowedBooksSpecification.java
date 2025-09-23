package com.unilib.api.books.repositories;

import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class BorrowedBooksSpecification implements Specification<Borrow> {
    @Override
    public Predicate toPredicate(Root<Borrow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }

    public static Specification<Borrow> isCompleted(){
        return (root,query,builder) -> builder.and(
                builder.equal(root.get("status"), BorrowStatus.IN_PROGRESS),
                builder.lessThan(root.get("expiresAt"), Instant.now())
        );
    }

    public static Specification<Borrow> isExpired() {
        return (root, query, builder) -> {
            Predicate pastReleaseDate = builder.and(
                    builder.isNotNull(root.get("releaseAt")),
                    builder.equal(root.get("status"), BorrowStatus.WAITING),
                    builder.lessThan(root.get("releaseAt"), Instant.now())
            );

            Predicate pastExpirationDate = builder.and(
                    builder.equal(root.get("status"), BorrowStatus.WAITING),
                    builder.lessThan(root.get("expiresAt"), Instant.now())
            );

            return builder.or(pastReleaseDate, pastExpirationDate);
        };
    }
}

