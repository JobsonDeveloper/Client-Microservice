package br.com.user.micro.dto.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Pagination information")
public class PageableResponseDto {
    private int pageNumber;
}