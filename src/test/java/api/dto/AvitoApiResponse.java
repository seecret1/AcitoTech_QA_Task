package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvitoApiResponse {
    private String status;
    private Result result;

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }

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