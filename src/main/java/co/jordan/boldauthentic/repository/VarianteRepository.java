package co.jordan.boldauthentic.repository;

import co.jordan.boldauthentic.model.Variante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VarianteRepository extends JpaRepository<Variante, Long> {
    Variante findBySku(String sku);
}
