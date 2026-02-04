package br.com.client.micro.controller.dto.Swagger;

import br.com.client.micro.controller.dto.ReturnAllClientsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Paginated response")
public class PageClientResponseDto {
    private List<ReturnAllClientsDto> content;
    private PageableResponseDto pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
}
