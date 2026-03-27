package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.SeatDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Seats", description = "Gerenciamento de assentos")
public interface SeatControllerDocs {

    @Operation(
            summary = "Buscar assento por ID",
            description = "Retorna os detalhes de um assento específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assento encontrado"),
            @ApiResponse(responseCode = "404", description = "Assento não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<SeatDTO> findById(
            @Parameter(description = "ID do assento", example = "1") Long id
    );


    @Operation(
            summary = "Listar assentos por sala",
            description = "Retorna todos os assentos de uma sala específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<List<SeatDTO>> findByRoomId(
            @Parameter(description = "ID da sala", example = "1") Long roomId
    );


    @Operation(
            summary = "Listar assentos disponíveis",
            description = "Retorna os assentos disponíveis para uma sessão específica dentro de uma sala."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<List<SeatDTO>> findAllAvailable(
            @Parameter(description = "ID da sessão", example = "10") Long sessionId,
            @Parameter(description = "ID da sala", example = "1") Long roomId
    );


    @Operation(
            summary = "Criar assento",
            description = "Cria um novo assento em uma sala."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<SeatDTO> create(SeatDTO dto);


    @Operation(
            summary = "Atualizar assento",
            description = "Atualiza os dados de um assento existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assento atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Assento ou sala não encontrados",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<SeatDTO> update(SeatDTO dto);


    @Operation(
            summary = "Remover assento",
            description = "Remove um assento do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assento removido"),
            @ApiResponse(responseCode = "404", description = "Assento não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> deleteSeat(
            @Parameter(description = "ID do assento", example = "1") Long id
    );
}
