package api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemRequest {
    @JsonProperty("sellerID")
    private Integer sellerID;
    private String name;
    private Integer price;
    private Statistics statistics;

    // Конструктор с параметрами
    public ItemRequest(Integer sellerID, String name, Integer price, Statistics statistics) {
        this.sellerID = sellerID;
        this.name = name;
        this.price = price;
        this.statistics = statistics;
    }

    // Getters and Setters
    public Integer getSellerID() { return sellerID; }
    public void setSellerID(Integer sellerID) { this.sellerID = sellerID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Statistics getStatistics() { return statistics; }
    public void setStatistics(Statistics statistics) { this.statistics = statistics; }
}