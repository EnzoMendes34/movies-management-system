package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.MovieDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Movies", description = "Gerenciamento de filmes")
public interface MovieControllerDocs {

    @Operation(
            summary = "Listar filmes",
            description = "Retorna uma lista paginada de filmes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Page<MovieDTO>> findAll(
            @Parameter(description = "Número da página", example = "0") Integer page,
            @Parameter(description = "Quantidade de itens por página", example = "12") Integer size,
            @Parameter(description = "Direção da ordenação: asc ou desc", example = "asc") String direction
    );


    @Operation(
            summary = "Buscar filme por ID",
            description = "Retorna os detalhes de um filme pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<MovieDTO> findById(
            @Parameter(description = "ID do filme", example = "1") Long id
    );


    @Operation(
            summary = "Buscar filmes por título",
            description = "Retorna uma lista paginada de filmes cujo título contém o valor informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Page<MovieDTO>> findByTitle(
            @Parameter(description = "Título do filme", example = "Batman") String title,
            @Parameter(description = "Número da página", example = "0") Integer page,
            @Parameter(description = "Quantidade de itens por página", example = "12") Integer size,
            @Parameter(description = "Direção da ordenação: asc ou desc", example = "asc") String direction
    );


    @Operation(
            summary = "Criar filme",
            description = "Cadastra um novo filme no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<MovieDTO> create(MovieDTO dto);


    @Operation(
            summary = "Atualizar filme",
            description = "Atualiza os dados de um filme existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<MovieDTO> update(MovieDTO dto);


    @Operation(
            summary = "Desativar filme",
            description = "Realiza um soft delete, marcando o filme como desativado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme desativado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<MovieDTO> disableMovie(
            @Parameter(description = "ID do filme", example = "1") Long id
    );
}
