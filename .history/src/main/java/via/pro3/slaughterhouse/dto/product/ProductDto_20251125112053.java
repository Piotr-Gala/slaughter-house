package via.pro3.slaughterhouse.dto.product;

import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;

import java.util.List;

public class ProductDto {
    public Long id;
    public String kind;
    public List<Long> partIds;

    public static ProductDto from(Product product) {
        ProductDto dto = new ProductDto();
        dto.id = product.getId();
        dto.kind = product.getKind() == null ? null : product.getKind().name();
        dto.partIds = product.getParts()
                .stream()
                .map(Part::getId)
                .toList();
        return dto;
    }
}
