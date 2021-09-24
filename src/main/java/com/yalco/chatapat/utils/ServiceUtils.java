package com.yalco.chatapat.utils;

import com.yalco.chatapat.entity.UserConnection;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class ServiceUtils {

    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
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
