package com.yalco.chatapat.repository;

import com.yalco.chatapat.entity.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {
    @Query(value = "select count(c)>0 from UserConnection c where (c.requester.username = :requester and c.bearer.username = :bearer) " +
            "or (c.requester.username = :bearer and c.bearer.username = :requester)")
    boolean existUserConnection(@Param("requester") String requester, @Param("bearer")String bearer);
}
