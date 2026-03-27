package EnzoMendes34.com.github.MoviesManagement.controllers;


import EnzoMendes34.com.github.MoviesManagement.controllers.docs.AuthControllerDocs;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.AccountCredentialsDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.TokenDTO;
import EnzoMendes34.com.github.MoviesManagement.services.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO credentials) {
        return ResponseEntity.ok(service.signin(credentials));
    }

    @PostMapping(value = "/signup")
    @Override
    public ResponseEntity<AccountCredentialsDTO> signUp(@RequestBody AccountCredentialsDTO credentials){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.signup(credentials));
    }

    @PostMapping("/refresh/{username}")
    @Override
    public ResponseEntity<TokenDTO> refreshToken(
            @PathVariable(value = "username") String username,
            @RequestHeader("Authorization") String refreshToken){
        if(parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.badRequest().build();
        }

        return  ResponseEntity.ok(service.refreshToken(username, refreshToken));
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }
}
