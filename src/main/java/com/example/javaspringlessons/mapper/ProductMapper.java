package com.example.javaspringlessons.mapper;

import org.example.rest.model.ProductDto;
import com.example.javaspringlessons.entity.Product;
import org.mapstruct.*;

@Mapper
public interface ProductMapper {

    @Mapping(target = "status", source = "quantity",qualifiedByName = "parseStatus")
    ProductDto toDto(Product product);

    Product toProduct(ProductDto productDTO);

    void updateProduct(@MappingTarget Product target, ProductDto source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchProduct(@MappingTarget Product target, ProductDto source);

    @Named("parseStatus")
    default ProductDto.StatusEnum parseStatus(Integer quantity){
        if (quantity == 0){
            return ProductDto.StatusEnum.OUT_OF_STOCK;
        }else {
            return ProductDto.StatusEnum.IN_STOCK;
        }
    }

}
