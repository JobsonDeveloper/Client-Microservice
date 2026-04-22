package br.com.client.micro.controller;

import br.com.client.micro.domain.complements.Address;
import br.com.client.micro.domain.complements.Phone;
import br.com.client.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.client.micro.dto.swagger.PageClientResponseDto;
import br.com.client.micro.domain.Client;
import br.com.client.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.client.micro.dto.request.ChangeClientDataDto;
import br.com.client.micro.dto.response.*;
import br.com.client.micro.service.imp.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Tag(name = "Client", description = "Client operations")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @DeleteMapping("/api/client/{id}/delete")
    @Operation(
            summary = "Delete a client",
            description = "Delete a client of the system",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inconsistent request fields",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponseMessageDto> deleteClient(
            @Parameter(description = "Client id", required = true)
            @PathVariable String id
    ) {
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseMessageDto("Client deleted successfully!")
        );
    }

    @PatchMapping("/api/client/update")
    @Operation(
            summary = "Update client information",
            description = "Update the information of a client",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client information updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientInfoDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inconsistent request fields",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ClientInfoDto> updateClientData(@Valid @RequestBody ChangeClientDataDto clientDto) {
        String id = clientDto.id();
        String firstName = clientDto.firstName();
        String lastName = clientDto.lastName();
        LocalDate birthday = clientDto.birthday();
        Phone phone = Phone.builder()
                .countryCode(clientDto.phone().countryCode())
                .cityCode(clientDto.phone().cityCode())
                .number(clientDto.phone().number())
                .build();
        Address address = Address.builder()
                .cep(clientDto.address().cep())
                .state(clientDto.address().state())
                .city(clientDto.address().city())
                .street(clientDto.address().street())
                .number(clientDto.address().number())
                .complement(clientDto.address().complement())
                .build();
        Client currentClient = Client.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .phone(phone)
                .address(address)
                .build();

        Client client = clientService.updateClient(currentClient);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ClientInfoDto(
                        "Client information updated successfully!",
                        new ClientDto(client)
                )
        );
    }

    @Operation(
            summary = "Get client information",
            description = "Return all information of a client",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client information returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientInfoDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping("/api/client/{id}/info")
    public ResponseEntity<ClientInfoDto> getClientInfo(
            @Parameter(description = "Client id", required = true)
            @PathVariable String id
    ) {
        Client client = clientService.getClient(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ClientInfoDto(
                        "Client information returned successfully!",
                        new ClientDto(client)
                )
        );
    }

    @GetMapping("/api/client/list")
    @Operation(
            summary = "List clients",
            description = "Return a list of clients",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client list returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = PageClientResponseDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageClientResponseDto> listClients(
            @RequestParam(defaultValue = "0", required = false, name = "page") int page,
            @RequestParam(defaultValue = "10", required = false, name = "size") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ClientDto> clients = clientService.listClients(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(PageClientResponseDto.from(clients));
    }
}
