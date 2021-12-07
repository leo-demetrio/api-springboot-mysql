package project.base.studiesspring.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPostRequestBody {
    @NotNull(message = "The product name cannot be null")
    @NotEmpty(message = "The product name cannot be null")
    private String name;
}
