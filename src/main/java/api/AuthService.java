package api;

import model.api.UserInfo;

import java.io.IOException;

public interface AuthService {
    UserInfo auth(String username, String password) throws IOException;
}
