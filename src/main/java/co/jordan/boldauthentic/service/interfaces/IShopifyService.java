package co.jordan.boldauthentic.service.interfaces;

import co.jordan.boldauthentic.dto.ProductDto;
import co.jordan.boldauthentic.dto.UpdateDto;
import co.jordan.boldauthentic.dto.VariantDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IShopifyService {
    List<ProductDto> fetchAllProducts() throws JsonProcessingException;
    String getNextPageUrl(List<String> linkHeaders);
    void  updateShopifyVariant(Long variantId);
    void setInventoryQuantity(Long inventoryItemId, int quantity);
    //VariantDto getShopifyVariant(Long variantId);

}

