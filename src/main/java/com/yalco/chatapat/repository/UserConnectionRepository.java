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

//    List<UserConnection> findAllByBearerUsernameAndConnectionRequestIsTrue(String username);

    @Query(value = "from UserConnection c where (c.bearer.username = :username or c.requester.username = :username) and " +
            "(c.connectionRequest = true and c.blocked = false and c.connected = false) and c.updatedBy <> :username")
    List<UserConnection> getAllPendingConnectionRequestByUsername(@Param("username") String username);

    @Query(value = "from UserConnection c where ((c.requester.username = :reviewer and c.bearer.username = :requester) or " +
            "(c.requester.username = :requester and c.bearer.username = :reviewer)) and " +
            "(c.connectionRequest = true and c.blocked = false and c.connected = false) and c.updatedBy <> :reviewer")
    Optional<UserConnection> findPendingConnectionRequestByParticipants(@Param("reviewer") String reviewer, @Param("requester")String requester);

    Optional<UserConnection> findByBearerUsernameAndRequesterUsername(String bearerUsername, String requesterUsername);

    @Query(value = "from UserConnection  c where c.bearer.username = :username or c.requester.username = :username")
    List<UserConnection> findAllConnectionsByParticipantUsername(@Param("username") String username);
}
