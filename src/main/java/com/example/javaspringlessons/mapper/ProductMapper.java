package com.example.javaspringlessons.mapper;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import org.mapstruct.*;

@Mapper
public interface ProductMapper {

    @Mapping(target = "status", source = "quantity",qualifiedByName = "parseStatus")
    ProductDTO toDto(Product product);

    Product toProduct(ProductDTO productDTO);

    void updateProduct(@MappingTarget Product target, ProductDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchProduct(@MappingTarget Product target, ProductDTO source);

    @Named("parseStatus")
    default ProductDTO.StatusEnum parseStatus(Integer quantity){
        if (quantity == 0){
            return ProductDTO.StatusEnum.OUT_OF_STOCK;
        }else {
            return ProductDTO.StatusEnum.IN_STOCK;
        }
    }

}
