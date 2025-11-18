package deki.ecommerce.common.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class Error implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Type type;
    @JsonProperty
    private String message;
    @JsonProperty
    private String messageKey;
    @JsonProperty
    private List<String> arguments;
    @JsonProperty
    private String property;

    public enum Type {
        FIELD_VALIDATION, INTERNAL_SERVER, BUSINESS_VALIDATION
    }

}
