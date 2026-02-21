package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final String uploadDir=System.getProperty("user.dir")+"/uploads/products/";

    @Override
    public ProductDto createProduct(ProductDto dto) {
       Category category=null;
       if(dto.getCategoryId()!=null){
           category=new Category();
           category.setId(dto.getCategoryId());
       }
        Product product=productMapper.toEntity(dto,category);
        Product saved=productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product is not found")) ;
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setQuantity(dto.getQuantity());
        if(dto.getCategoryId()!=null){
            Category category=new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product is not found"));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto updateImage(Long id, MultipartFile file) throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size should be less than 5MB");
        }

        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new RuntimeException("File must be jpeg, png, or jpg");
        }

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();

        String fileName = UUID.randomUUID() + "." + ext;

        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Path filePath = Paths.get(uploadDir, fileName);

        Files.write(filePath, file.getBytes());   // ✅ fixed

        product.setImageUrl("/products/images/" + fileName);

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String direction) {
        Sort sort=direction.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Product> productPage=productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchProducts(String keyword, int page, int size) {
        if(keyword == null || keyword.isBlank()) {
            return getAllProducts(page, size, "id", "asc");
        }
        Pageable pageable=PageRequest.of(page,size);
        Page<Product> productPage=productRepository.searchProducts(keyword,pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Page<Product> productPage=productRepository.advanceFilter(null,categoryId,minPrice,maxPrice,pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String direction) {
        Sort sort=direction.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Product> productPage=productRepository.advanceFilter(keyword,categoryId,minPrice,maxPrice,pageable);
        return productPage.map(productMapper::toDto);
    }
}
