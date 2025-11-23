package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Statistics {

    private Integer likes;

    private Integer viewCount;

    private Integer contacts;
}
