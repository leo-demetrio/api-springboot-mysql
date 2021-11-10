package project.base.studiesspring.requests;

import lombok.Data;

@Data
public class ProductPutRequestBody {
    private Long id;
    private String name;
}
