package br.com.client.micro.dto.swagger;

import br.com.client.micro.dto.response.ClientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Schema(description = "Paginated response")
public class PageClientResponseDto {

    private List<ClientDto> content;
    private PageableResponseDto pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;

    public static PageClientResponseDto from(Page<ClientDto> page) {
        PageClientResponseDto dto = new PageClientResponseDto();

        dto.setContent(page.getContent());

        PageableResponseDto pageableDto = new PageableResponseDto();
        pageableDto.setPageNumber(page.getNumber());

        dto.setPageable(pageableDto);
        dto.setLast(page.isLast());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements(page.getTotalElements());
        dto.setSize(page.getSize());

        return dto;
    }
}