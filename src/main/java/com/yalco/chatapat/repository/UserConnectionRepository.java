package com.yalco.chatapat.repository;

import com.yalco.chatapat.entity.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {
    @Query(value = "select count(c)>0 from UserConnection c where (c.requester.username = :requester and c.bearer.username = :bearer) " +
            "or (c.requester.username = :bearer and c.bearer.username = :requester)")
    boolean existUserConnection(@Param("requester") String requester, @Param("bearer")String bearer);

    @Query(value = "from UserConnection c where (c.requester.username = :requester and c.bearer.username = :bearer) " +
            "or (c.requester.username = :bearer and c.bearer.username = :requester)")
    Optional<UserConnection> findUserConnectionByParticipants(@Param("requester") String requester, @Param("bearer")String bearer);

    List<UserConnection> findAllByBearerUsernameAndConnectionRequestIsTrue(String username);

    Optional<UserConnection> findByBearerUsernameAndRequesterUsername(String bearerUsername, String requesterUsername);
}
