package co.jordan.boldauthentic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantDto {

    @JsonProperty("id")
    private Long idVariante;

    private String sku;

    @JsonProperty("inventory_item_id")
    private Long inventoryItemId;

    @JsonProperty("inventory_quantity")
    private Integer inventoryQuantity;
}
