package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.AccountCredentialsDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.TokenDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.models.User;
import EnzoMendes34.com.github.MoviesManagement.repositories.UserRepository;
import EnzoMendes34.com.github.MoviesManagement.security.jwt.JwtTokenProvider;
import EnzoMendes34.com.github.MoviesManagement.types.UserTypes;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class AuthService {

    private final UserRepository repository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repository, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.repository = repository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    public TokenDTO signin(AccountCredentialsDTO credentials){
        //checa e valida as credenciais enviadas
        if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new BadCredentialsException("It's not possible to signin with null credentials");
        }

        User user = repository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        //validando a senha com o authenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        //colocando os roles do usuário no token
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return tokenProvider.createAccessToken(user.getUsername(), roles);
    }

    public AccountCredentialsDTO signup(AccountCredentialsDTO credentials){
        //checa e valida as credenciais enviadas
        if(credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new BadCredentialsException("It's not possible to signup with null credentials");
        }

        if(repository.existsByUsername(credentials.getUsername())){
            throw new BadCredentialsException("User already exists");
        }
        if(repository.existsByEmail(credentials.getEmail())) {
            throw new BadCredentialsException("E-mail already exists.");
        }

        User user = new User();
        user.setEmail(credentials.getEmail());
        user.setUsername(credentials.getUsername());
        user.setPassword(encoder.encode(credentials.getPassword()));
        user.setRole(UserTypes.USER);

        repository.save(user);

        return credentials;
    }

    public TokenDTO refreshToken(String username, String refreshToken){
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return tokenProvider.refreshAccessToken(refreshToken);
    }
}
