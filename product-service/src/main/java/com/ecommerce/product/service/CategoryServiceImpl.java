package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category=categoryMapper.toEntity(dto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category=categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category is not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found");
        }

        if(!categoryRepository.findByParentId(id).isEmpty()){
            throw new RuntimeException("Cannot delete parent category");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category is not found"));
        return categoryMapper.toDto(category) ;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
    }
}
