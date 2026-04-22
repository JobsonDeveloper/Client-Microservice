package br.com.user.micro.dto.swagger;

import br.com.user.micro.dto.response.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Schema(description = "Paginated response")
public class PageUserResponseDto {

    private List<UserDto> content;
    private PageableResponseDto pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;

    public static PageUserResponseDto from(Page<UserDto> page) {
        PageUserResponseDto dto = new PageUserResponseDto();

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