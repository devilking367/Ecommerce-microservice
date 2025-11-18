package deki.ecommerce.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import deki.ecommerce.common.dto.error.Error;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private T data;

    @JsonProperty("timestamp")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private ZonedDateTime timestamp = ZonedDateTime.now();

    @JsonProperty("errors")
    private List<Error> errors;

    @JsonProperty("page")
    private Pageable page;
}
