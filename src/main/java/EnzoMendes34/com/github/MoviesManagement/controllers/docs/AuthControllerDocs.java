package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.AccountCredentialsDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.auth.TokenDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Endpoints responsáveis pela autenticação e gerenciamento de tokens")
public interface AuthControllerDocs {

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário com username e password e retorna um JWT contendo accessToken e refreshToken."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Falha na autenticação",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials);


    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Cria um novo usuário no sistema. Username e email devem ser únicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AccountCredentialsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Usuário ou email já existente",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<AccountCredentialsDTO> signUp(AccountCredentialsDTO credentials);


    @Operation(
            summary = "Atualizar token",
            description = "Gera um novo accessToken a partir de um refreshToken válido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<TokenDTO> refreshToken(
            @Parameter(description = "Username do usuário", example = "enzo123")
            String username,

            @Parameter(
                    description = "Refresh token no header Authorization",
                    example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
            )
            String refreshToken
    );
}