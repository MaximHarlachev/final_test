package model.api;

import java.util.Objects;

public class UserInfo {
    private String userToken;
    private String role;
    private String displayName;
    private String login;

    public UserInfo(){}

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(userToken, userInfo.userToken) && Objects.equals(role, userInfo.role) && Objects.equals(displayName, userInfo.displayName) && Objects.equals(login, userInfo.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userToken, role, displayName, login);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userToken='" + userToken + '\'' +
                ", role='" + role + '\'' +
                ", displayName='" + displayName + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
