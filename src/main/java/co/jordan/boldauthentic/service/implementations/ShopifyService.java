package co.jordan.boldauthentic.service.implementations;

import co.jordan.boldauthentic.dto.ProductDto;
import co.jordan.boldauthentic.dto.ResponseDto;
import co.jordan.boldauthentic.dto.UpdateDto;
import co.jordan.boldauthentic.dto.VariantDto;
import co.jordan.boldauthentic.service.interfaces.IShopifyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Log4j2
public class ShopifyService implements IShopifyService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Value("${application.shopify.url}")
    private String shopifyUrl;

    @Value("${application.shopify.token}")
    private String shopifyToken;

    @Value("${application.shopify.location-id}")
    private String locationId;



    @Override
    public List<ProductDto> fetchAllProducts() throws JsonProcessingException {


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyToken);

        List<ProductDto> allProducts = new ArrayList<>();
        boolean hasNextPage = true;

        String url = shopifyUrl + "/products.json?limit=250";
        //log.info("shopifyUrl: " + shopifyUrl);

        while (hasNextPage) {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ResponseDto shopifyResponse = objectMapper.readValue(response.getBody(), ResponseDto.class);
                allProducts.addAll(shopifyResponse.getProducts());

                // Récupération de la page suivante
                url = getNextPageUrl(response.getHeaders().get("Link"));
                hasNextPage = (url != null);
            } else {
                throw new RuntimeException("Erreur Shopify : " + response.getStatusCode());
            }
        }

        return allProducts;
    }

    @Override
    public String getNextPageUrl(List<String> linkHeaders) {
        if (linkHeaders == null || linkHeaders.isEmpty()) return null;

        Pattern pattern = Pattern.compile("<(.*?)>; rel=\"next\"");
        for (String header : linkHeaders) {
            Matcher matcher = pattern.matcher(header);
            if (matcher.find()) {
                return matcher.group(1); // Retourne l'URL de la page suivante
            }
        }
        return null;
    }

    @Override
    public void updateShopifyVariant(Long variantId) {

        String url = shopifyUrl + "/variants/" + variantId + ".json";

        //System.out.println(url + shopifyUrl);

        // Construire la requête JSON
        String jsonPayload = "{ \"variant\": { \"id\": " + variantId + ", \"inventory_management\": \"shopify\" } }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Shopify-Access-Token", shopifyToken);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erreur Shopify lors de la mise à jour de la variante " + variantId);
        }
    }

    @Override
    public void setInventoryQuantity(Long inventoryItemId, int quantity) {
        String url = this.shopifyUrl + "/inventory_levels/set.json";

        String jsonPayload = "{ \"inventory_item_id\": " + inventoryItemId + ", \"location_id\":"+ locationId +" , \"available\": " + quantity + " }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Shopify-Access-Token", shopifyToken);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erreur Shopify lors de la mise à jour de la quantité pour inventory_item_id " + inventoryItemId);
        }
    }

    /*
    @Override
    public VariantDto getShopifyVariant(Long variantId) {
        String url = shopifyUrl + "/variants/" + variantId + ".json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                return objectMapper.readValue(response.getBody(), VariantDto.class);

            } catch (Exception e) {
                throw new RuntimeException("Erreur lors du parsing JSON pour la variante " + variantId, e);
            }
        } else {
            throw new RuntimeException("Erreur Shopify lors de la récupération de la variante " + variantId + ". Status: " + response.getStatusCode());
        }

    }

     */


}
