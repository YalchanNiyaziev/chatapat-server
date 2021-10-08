package com.yalco.chatapat.utils;

import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.exception.OperationNotAllowedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.Objects;

public class ServiceUtils {

    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    public static void checkSelfOperation(String username) {
        if (username == null || !Objects.equals(username, getAuthenticatedUsername())) {
            throw new OperationNotAllowedException("Can not execute operation. Your account not has rights to make changes on this user.");
        }
    }

    public static boolean isConnected(UserConnection connection) {
        Assert.notNull(connection, "Must be provided valid connection");
        return connection.isConnected() && !connection.isBlocked() && !connection.isConnectionRequest();
    }

    public static boolean isConnectionRequested(UserConnection connection) {
        Assert.notNull(connection, "Must be provided valid connection");
        return connection.isConnectionRequest() && !connection.isConnected() && !connection.isBlocked();
    }

    public static boolean isRemoved(UserConnection connection) {
        Assert.notNull(connection, "Must be provided valid connection");
        return !connection.isConnectionRequest() && !connection.isConnected() && !connection.isBlocked();
    }

    public static boolean isBlocked(UserConnection connection) {
        Assert.notNull(connection, "Must be provided valid connection");
        return connection.isBlocked() && !connection.isConnected() && !connection.isConnectionRequest();
    }
}
