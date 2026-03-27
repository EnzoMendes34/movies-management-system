package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.SessionDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sessions", description = "Gerenciamento de sessões.")
public interface SessionControllerDocs {

    @Operation(
            summary = "Listar todas as sessões",
            description = "Retorna uma lista paginada de todas as sessões cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    ResponseEntity<Page<SessionDTO>> findAll(
            @Parameter(description = "Número da página", example = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "12") Integer size,
            @Parameter(description = "Direção da ordenação (asc/desc)", example = "asc") String direction
    );

    @Operation(
            summary = "Buscar sessão por ID",
            description = "Retorna os detalhes de uma sessão específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão encontrada"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<SessionDTO> findById(
            @Parameter(description = "ID da sala", example = "1") Long id
    );

    @Operation(
            summary = "Busca sessão por filmeId e status.",
            description = "Retorna todas as sessões do filme filtrada por status (e.g., ACTIVE, CANCELLED)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessões encontradas",
                    content = @Content(schema = @Schema(implementation = SessionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    ResponseEntity<List<SessionDTO>> findByMovieIdAndStatus(
            @Parameter(description = "ID do filme", example = "10")
            @PathVariable Long movieId,

            @Parameter(description = "Status da sessão", example = "ACTIVE")
            @RequestParam SessionStatus status
    );

    @Operation(
            summary = "Cria uma nova sessão",
            description = "Cria uma nova sessão com filme, sala, tempo de duração e informação de preço"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso",
                    content = @Content(schema = @Schema(implementation = SessionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de sessão (Confronto de horário)")
    })
    ResponseEntity<SessionDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da sessão",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SessionDTO.class),
                            examples = @ExampleObject(value = """
                            {
                                "movieId": 1,
                                "roomId": 2,
                                "startTime": "2026-03-27T18:00:00",
                                "endTime": "2026-03-27T20:30:00",
                                "language": "DUBBED",
                                "priceInCents": 3000,
                                "status": "ACTIVE",
                                "discountPercentage": 10
                            }
                            """)
                    )
            )
            @RequestBody SessionDTO dto
    );

    @Operation(
            summary = "Atualiza uma sessão",
            description = "Atualiza uma sessão existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = SessionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Input incorreto"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito de sessão")
    })
    ResponseEntity<SessionDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados de sessão atualizados",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SessionDTO.class))
            )
            @RequestBody SessionDTO dto
    );

    @Operation(
            summary = "Cancela uma sessão",
            description = "Cancela uma sessão mudando seu status para CANCELLED"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão cancelada com sucesso",
                    content = @Content(schema = @Schema(implementation = SessionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    ResponseEntity<SessionDTO> cancelSession(
            @Parameter(description = "Session ID", example = "1")
            @PathVariable Long sessionId
    );
}
