package co.jordan.boldauthentic.controller;

import co.jordan.boldauthentic.dto.UpdateDto;
import co.jordan.boldauthentic.service.interfaces.IVarianteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
public class VarianteController {

    private final IVarianteService varianteService;

    @GetMapping("/update-database")
    public ResponseEntity<String> saveAllVariants(){
        return  ResponseEntity.ok(varianteService.syncVariants());
    }

    @GetMapping("/active-inventory-management")
    public ResponseEntity<String> getActiveInventoryManagement(){
        return ResponseEntity.ok(varianteService.activeInventoryManagement());
    }

    @PostMapping("/update-inventory")
    public ResponseEntity<String> updateInventory(
            @RequestBody UpdateDto updateDto
    )
    {

        return ResponseEntity.ok(varianteService.updateInventoryQuantity(updateDto));
    }

}
