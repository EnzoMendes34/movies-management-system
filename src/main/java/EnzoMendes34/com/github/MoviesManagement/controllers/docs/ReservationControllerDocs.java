package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.ReservationDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.request.ReservationRequestDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Reservations", description = "Gerenciamento de reservas de sessões")
public interface ReservationControllerDocs {

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Retorna os detalhes de uma reserva específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ReservationDTO> findById(
            @Parameter(description = "ID da reserva", example = "1") Long id
    );


    @Operation(
            summary = "Listar reservas do usuário",
            description = "Retorna uma lista paginada de reservas associadas a um usuário."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Page<ReservationDTO>> findByUserId(
            @Parameter(description = "ID do usuário", example = "5") Long userId,
            @Parameter(description = "Número da página", example = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "12") Integer size,
            @Parameter(description = "Direção da ordenação", example = "asc") String direction
    );


    @Operation(
            summary = "Criar reserva",
            description = "Cria uma nova reserva para uma sessão específica, bloqueando os assentos informados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou assentos indisponíveis",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou sessão não encontrados",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ReservationDTO> createReservation(ReservationRequestDTO dto);


    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela uma reserva caso ainda esteja pendente. Reservas confirmadas não podem ser canceladas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<ReservationDTO> cancelReservation(
            @Parameter(description = "ID da reserva", example = "1") Long id
    );
}
