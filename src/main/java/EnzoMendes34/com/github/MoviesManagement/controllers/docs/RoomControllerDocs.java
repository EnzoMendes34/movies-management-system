package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.RoomDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Rooms", description = "Gerenciamento de salas de cinema")
public interface RoomControllerDocs {

    @Operation(
            summary = "Listar todas as salas",
            description = "Retorna uma lista paginada de todas as salas cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    ResponseEntity<Page<RoomDTO>> findAll(
            @Parameter(description = "Número da página", example = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "12") Integer size,
            @Parameter(description = "Direção da ordenação (asc/desc)", example = "asc") String direction
    );


    @Operation(
            summary = "Listar salas ativas",
            description = "Retorna todas as salas habilitadas (enabled = true)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    ResponseEntity<List<RoomDTO>> findAllEnabled();


    @Operation(
            summary = "Buscar sala por ID",
            description = "Retorna os detalhes de uma sala específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala encontrada"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<RoomDTO> findById(
            @Parameter(description = "ID da sala", example = "1") Long id
    );


    @Operation(
            summary = "Criar sala",
            description = "Cria uma nova sala de cinema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sala criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<RoomDTO> create(RoomDTO dto);


    @Operation(
            summary = "Atualizar sala",
            description = "Atualiza os dados de uma sala existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<RoomDTO> update(RoomDTO dto);


    @Operation(
            summary = "Desabilitar sala",
            description = "Desativa uma sala (soft delete)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala desabilitada"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<RoomDTO> disable(
            @Parameter(description = "ID da sala", example = "1") Long id
    );
}