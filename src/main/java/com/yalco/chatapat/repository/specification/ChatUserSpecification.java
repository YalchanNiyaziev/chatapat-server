package com.yalco.chatapat.repository.specification;

import com.yalco.chatapat.dto.AddressDto;
import com.yalco.chatapat.dto.SearchChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChatUserSpecification implements Specification<ChatUser> {

    private final SearchChatUserDto chatUserSearch;

    @Override
    public Predicate toPredicate(Root<ChatUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        // TODO add root=root.join(UserConnection) when make relation many to many and filter unblocked connections here
        List<Predicate> predicates = new ArrayList<>();
        if (chatUserSearch == null) {
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }

        if (StringUtils.hasText(chatUserSearch.getUsername())) {
            predicates.add(criteriaBuilder.equal(root.get("username"), chatUserSearch.getUsername()));
        }
        if (StringUtils.hasText(chatUserSearch.getChatName())) {
            predicates.add(criteriaBuilder.like(root.get("chatName"), "%" + chatUserSearch.getChatName() + "%"));
        }
        if (StringUtils.hasText(chatUserSearch.getFirstName())) {
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + chatUserSearch.getFirstName() + "%"));
        }
        if (StringUtils.hasText(chatUserSearch.getLastName())) {
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + chatUserSearch.getLastName() + "%"));
        }
        if (chatUserSearch.getBirthDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("birthDate"), chatUserSearch.getBirthDate()));
        }
        if (chatUserSearch.getGender() != null) {
            predicates.add(criteriaBuilder.equal(root.get("gender"), chatUserSearch.getGender()));
        }
        if(chatUserSearch.getRole() != null) {
            predicates.add(criteriaBuilder.equal(root.get("role"), chatUserSearch.getRole()));
        }
        if(chatUserSearch.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), chatUserSearch.getStatus()));
        }
        if(chatUserSearch.getLocked() != null) {
            predicates.add(criteriaBuilder.equal(root.get("locked"), chatUserSearch.getLocked()));
        }
        if(chatUserSearch.getClosed() != null) {
            predicates.add(criteriaBuilder.equal(root.get("closed"), chatUserSearch.getClosed()));
        }


        // TODO add some predicates for admin filtering, EXAMPLE registration TS before and after

        if (chatUserSearch.getAddress() != null) {
            AddressDto address = chatUserSearch.getAddress();
            if (StringUtils.hasText(address.getCountry())) {
                predicates.add(criteriaBuilder.equal(root.get("address").get("country"), address.getCountry()));
            }
            if (StringUtils.hasText(address.getCity())) {
                predicates.add(criteriaBuilder.like(root.get("address").get("city"), "%" + address.getCity() + "%"));
            }
            if (StringUtils.hasText(address.getStreet())) {
                predicates.add(criteriaBuilder.like(root.get("address").get("street"), "%" + address.getStreet() + "%"));
            }
            if (StringUtils.hasText(address.getPostCode())) {
                predicates.add(criteriaBuilder.equal(root.get("address").get("postCode"), address.getPostCode()));
            }
        }

        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("username")));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
