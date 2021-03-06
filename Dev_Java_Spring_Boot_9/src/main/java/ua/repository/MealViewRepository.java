package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ua.model.filter.MealFilter;
import ua.model.view.MealView;

public interface MealViewRepository {
	Page<MealView> findAll(MealFilter filter, Pageable pageable,Sort sort);
	Page<MealView> findAllByIdCafe(MealFilter filter, Pageable pageable,Sort sort,Integer id);
}
