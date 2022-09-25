import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.auth.LoginRequest;
import com.example.rest.payload.auth.LoginResponse;
import com.example.rest.payload.auth.SignUpRequest;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PopulateDatabase {

    static final String API_URI_AUTH = "http://localhost:8080/api/auth/";
    static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static void main(String[] args) {
        List<String> tokens = new ArrayList<>();
        for (String name : readFile("names.txt")) {
//            signUpUser(name);
            tokens.add(loginUser(name));
        }
        System.out.println(tokens);
    }

    private static void signUpUser(String name) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(name + "@gmail.com");
        signUpRequest.setUsername(name);
        signUpRequest.setPassword(name);

        GenericResponse signUpResponse = REST_TEMPLATE.postForObject(API_URI_AUTH + "signup", signUpRequest, GenericResponse.class);
        if (signUpResponse == null || signUpResponse.getCode() != 0) {
            throw new RuntimeException("Error creating user with name: " + name);
        }
    }

    private static String loginUser(String name) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(name);
        loginRequest.setPassword(name);
        LoginResponse loginInResponse = REST_TEMPLATE.postForObject(API_URI_AUTH + "login", loginRequest, LoginResponse.class);
        if (loginInResponse == null || loginInResponse.getCode() != 0) {
            throw new RuntimeException("Error logging in user with name: " + name);
        }
        return loginInResponse.getToken();
    }

    private static List<String> readFile(String fileName) {
        try {
            ClassLoader classLoader = PopulateDatabase.class.getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            Scanner s = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
