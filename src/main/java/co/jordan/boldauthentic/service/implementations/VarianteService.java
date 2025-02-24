package co.jordan.boldauthentic.service.implementations;

import co.jordan.boldauthentic.dto.ProductDto;
import co.jordan.boldauthentic.dto.UpdateDto;
import co.jordan.boldauthentic.dto.VariantDto;
import co.jordan.boldauthentic.model.Variante;
import co.jordan.boldauthentic.repository.VarianteRepository;
import co.jordan.boldauthentic.service.interfaces.IShopifyService;
import co.jordan.boldauthentic.service.interfaces.IVarianteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarianteService implements IVarianteService {


    @Value("${application.shopify.url}")
    private String shopifyUrl;

    @Value("${application.shopify.token}")
    private String shopifyToken;

    private final IShopifyService shopifyService;
    private final VarianteRepository varianteRepository;


    @Override
    @Transactional
    public String syncVariants() {
        try {
            List<ProductDto> products = shopifyService.fetchAllProducts();

            for (ProductDto product : products) {
                saveVariants(product.getVariants());
            }

            return "Synchronisation réussie ! " + products.size() + " produits récupérés.";
        } catch (Exception e) {
            return "Erreur lors de la synchronisation : " + e.getMessage();
        }
    }

    @Override

    public void saveVariants(List<VariantDto> variantDTOs) {
        for (VariantDto dto : variantDTOs) {

            varianteRepository.save(
                    Variante.
                            builder()
                                .sku(dto.getSku())
                                .idVariante(dto.getIdVariante())
                                .inventoryItemId(dto.getInventoryItemId())
                        //.inventoryQuantity(dto.getInventoryQuantity())
                        .build()
            );
        }
    }

    @Override
    public String activeInventoryManagement() {
        List<Variante> variants = varianteRepository.findAll();
        for ( Variante variante : variants) {
            shopifyService.updateShopifyVariant(variante.getIdVariante());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            shopifyService.setInventoryQuantity(variante.getInventoryItemId(), 1000);
        }
        return "Mise à jour effectuée";
    }

    @Override
    public String updateInventoryQuantity(UpdateDto dto) {
        Variante variante = varianteRepository.findBySku(dto.getCodeBar());

        if (variante == null) return "Variante n'existe pas";

        var qte = 0;
        if (dto.isState()) qte = 1000;

        shopifyService.setInventoryQuantity(variante.getInventoryItemId(),qte);
        return "";
    }
}
