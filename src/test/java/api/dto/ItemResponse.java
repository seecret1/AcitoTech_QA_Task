package api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemResponse {

    private String id;

    @JsonProperty("sellerId")
    private Integer sellerId;

    private String name;

    private Integer price;

    private Statistics statistics;

    private String createdAt;
}