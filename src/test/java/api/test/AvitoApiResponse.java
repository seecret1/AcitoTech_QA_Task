package api.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvitoApiResponse {
    private String status;
    private Result result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String message;

        @JsonProperty("messages")
        private Object messages;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getMessages() { return messages; }
        public void setMessages(Object messages) { this.messages = messages; }
    }
}