package api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRequest {

    @JsonProperty("sellerID")
    private Integer sellerID;

    private String name;

    private Integer price;

    private Statistics statistics;
}