package project.base.studiesspring.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductPostRequestBody {
    @NotNull(message = "The product name cannot be null")
    @NotEmpty(message = "The product name cannot be null")
    private String name;
}
