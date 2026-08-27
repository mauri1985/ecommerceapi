package com.mauhernandez.ecommerceapi.specification;

import com.mauhernandez.ecommerceapi.model.Producto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductoSpecifications {

    public static Specification<Producto> activo() {
        return (root, query, cb) -> cb.isTrue(root.get("activo"));
    }

    public static Specification<Producto> categoriaIdEnLista(List<Long> categoriaIds) {
        if (categoriaIds == null || categoriaIds.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> root.get("categoria").get("id").in(categoriaIds);
    }

    public static Specification<Producto> nombreOCategoriaContiene(String q) {
        if (q == null || q.isBlank()) {
            return null;
        }
        String patron = "%" + q.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("nombre")), patron),
                cb.like(cb.lower(root.get("categoria").get("nombre")), patron)
        );
    }

    public static Specification<Producto> precioEntre(java.math.BigDecimal min, java.math.BigDecimal max) {
        if (min == null && max == null) {
            return null;
        }
        return (root, query, cb) -> {
            if (min != null && max != null) {
                return cb.between(root.get("precio"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("precio"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("precio"), max);
            }
        };
    }

    public static Specification<Producto> tieneAtributo(String clave, List<String> valores) {
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> {
            var valorAtributo = cb.function("jsonb_extract_path_text", String.class, root.get("atributos"), cb.literal(clave));
            var predicados = valores.stream()
                    .map(v -> cb.equal(cb.lower(valorAtributo), v.toLowerCase()))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new);
            return cb.or(predicados);
        };
    }
}