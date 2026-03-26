package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.AccountCredentialsDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.TokenDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.models.User;
import EnzoMendes34.com.github.MoviesManagement.repositories.UserRepository;
import EnzoMendes34.com.github.MoviesManagement.security.jwt.JwtTokenProvider;
import EnzoMendes34.com.github.MoviesManagement.types.UserTypes;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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

        //validando a senha com o authenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        //Busca os dados
        User user = repository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        //colocando os roles do usuário no token
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return tokenProvider.createAccessToken(user.getUsername(), roles);
    }

    public AccountCredentialsDTO signup(AccountCredentialsDTO credentials){
        //checa e valida as credenciais enviadas
        ValidationUtils.validateRequiredFields(Map.of(
                "username", credentials.getUsername(),
                "password", credentials.getPassword()
        ));

        if(repository.existsByUsername(credentials.getUsername())){
            throw new BusinessException("User already exists");
        }
        if(repository.existsByEmail(credentials.getEmail())) {
            throw new BusinessException("E-mail already exists.");
        }

        User user = new User();
        user.setEmail(credentials.getEmail());
        user.setUsername(credentials.getUsername());
        user.setPassword(generateHashedPassword(credentials.getPassword()));
        user.setRole(UserTypes.USER);

        repository.save(user);

        return credentials;
    }

    public TokenDTO refreshToken(String username, String refreshToken){
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return  tokenProvider.refreshAccessToken(refreshToken);
    }

    private String generateHashedPassword(String password){
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8,
                185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(passwordEncoder);;
        return passwordEncoder.encode(password);

    }
}
