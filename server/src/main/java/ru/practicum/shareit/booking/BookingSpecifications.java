package ru.practicum.shareit.booking;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.shareit.utils.BookingStatus;
import ru.practicum.shareit.utils.StateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingSpecifications {

    public static Specification<Booking> byUserAndState(Long userId, StateRequest state) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Условие по booker_id
            predicates.add(criteriaBuilder.equal(root.get("booker").get("id"), userId));

            // Условия по состояниям
            return makeStateCondition(state, root, criteriaBuilder, predicates);
        };
    }

    public static Specification<Booking> byOwnerAndState(Long userId, StateRequest state) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Условие по owner_id
            predicates.add(criteriaBuilder.equal(root.get("item").get("owner").get("id"), userId));

            // Условия по состояниям
            return makeStateCondition(state, root, criteriaBuilder, predicates);
        };
    }

    private static Predicate makeStateCondition(StateRequest state,
                                                Root<Booking> root,
                                                CriteriaBuilder criteriaBuilder,
                                                List<Predicate> predicates) {
        switch (state) {
            case CURRENT -> predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get("start"), LocalDateTime.now()),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("end"), LocalDateTime.now()),
                            criteriaBuilder.equal(root.get("status"), BookingStatus.APPROVED)
                    ));
            case PAST -> predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.lessThan(root.get("end"), LocalDateTime.now()),
                            criteriaBuilder.equal(root.get("status"), BookingStatus.APPROVED)
                    ));
            case FUTURE -> predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.greaterThan(root.get("start"), LocalDateTime.now()),
                            criteriaBuilder.equal(root.get("status"), BookingStatus.APPROVED)
                    ));
            case WAITING -> predicates.add(
                    criteriaBuilder.equal(root.get("status"), BookingStatus.WAITING)
            );
            case REJECTED -> predicates.add(
                    criteriaBuilder.equal(root.get("status"), BookingStatus.REJECTED)
            );
            default -> predicates.add(
                    root.get("status").in(
                            BookingStatus.APPROVED,
                            BookingStatus.REJECTED,
                            BookingStatus.CANCELED,
                            BookingStatus.WAITING
                    )
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
