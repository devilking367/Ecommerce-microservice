package deki.ecommerce.product.domain.product;

import deki.ecommerce.platform.model.data.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AbstractAuditableEntity {
    @NotNull(message = "validation.constraints.NotBlank.message")
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @NotBlank(message = "validation.constraints.NotBlank.message")
    @Column(nullable = false, updatable = false)
    private String code;

    @Column
    private String imageUrl;

    @Column
    private Boolean active = true;
}

