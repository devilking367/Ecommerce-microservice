package deki.ecommerce.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pageable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_totalElements")
    private long totalElements;

    @JsonProperty("_currentPage")
    private int currentPage;

    @JsonProperty("_pageSize")
    private int pageSize;

    @JsonProperty("_totalPages")
    private int totalPages;
}
