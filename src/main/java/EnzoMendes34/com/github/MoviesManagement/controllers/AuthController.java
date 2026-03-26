package EnzoMendes34.com.github.MoviesManagement.controllers;


import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.AccountCredentialsDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.TokenDTO;
import EnzoMendes34.com.github.MoviesManagement.services.AuthService;
import com.stripe.model.Token;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoints.")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO credentials) {
        return ResponseEntity.ok(service.signin(credentials));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<AccountCredentialsDTO> signUp(@RequestBody AccountCredentialsDTO credentials){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.signup(credentials));
    }

    @PutMapping("/refresh/{username}")
    public ResponseEntity<TokenDTO> refreshToken(
            @PathVariable(value = "username") String username,
            @RequestHeader("Authorization") String refreshToken){

        return  ResponseEntity.ok(service.refreshToken(username, refreshToken));
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.getPassword()) ||
                StringUtils.isBlank(credentials.getUsername());
    }
}
