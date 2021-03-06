package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.entity.Ingredient;
import ua.model.filter.SimpleFilter;

public interface IngredientService extends CrudService<Ingredient, Integer> {
	Page<Ingredient> findAll(Pageable pageable, SimpleFilter filter);

	Page<Ingredient> findAll(Pageable pageable);
}
