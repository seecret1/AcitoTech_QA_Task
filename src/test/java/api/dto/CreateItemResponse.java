package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateItemResponse {
    private String status;

    public String extractItemId() {
        if (status != null && status.contains(" - ")) {
            return status.split(" - ")[1];
        }
        return null;
    }
}