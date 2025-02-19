package co.jordan.boldauthentic.service.interfaces;

import co.jordan.boldauthentic.dto.UpdateDto;
import co.jordan.boldauthentic.dto.VariantDto;
import co.jordan.boldauthentic.model.Variante;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IVarianteService {

    String syncVariants();
    void saveVariants(List<VariantDto> variantDTOs);
    String activeInventoryManagement();
    String updateInventoryQuantity(UpdateDto dto) ;
}
