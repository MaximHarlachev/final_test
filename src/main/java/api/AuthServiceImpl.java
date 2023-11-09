package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.api.UserInfo;
import okhttp3.*;

import java.io.IOException;

public class AuthServiceImpl implements AuthService {
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=UTF-8");
    private static final String PATH = "auth/login";
    private final String BASE_PATH;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public AuthServiceImpl(OkHttpClient client, String url) {
        this.BASE_PATH = url;
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    @Override
    public UserInfo auth(String username, String password) throws IOException {
        String body = "{\"username\" : \"" + username + "\", \"password\" : \"" + password + "\"}";
        RequestBody jsonBody = RequestBody.create(body, APPLICATION_JSON);
        Request request = new Request.Builder().post(jsonBody).url(BASE_PATH + PATH).build();
        Response response = this.client.newCall(request).execute();
        return mapper.readValue(response.body().string(), UserInfo.class);
    }
}
