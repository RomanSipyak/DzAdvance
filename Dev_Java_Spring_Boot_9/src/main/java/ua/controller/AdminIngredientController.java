//package ua.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.bind.support.SessionStatus;
//
//
//import ua.entity.Ingredient;
//
//import ua.service.IngredientService;
//
//@Controller
//@RequestMapping("/admin/ingredient")
//@SessionAttributes("ingredient")
//public class AdminIngredientController {
//
//	private final IngredientService service;
//	
//	@Autowired
//	public AdminIngredientController(IngredientService service) {
//		this.service = service;
//	}
//	@ModelAttribute("ingredient")
//	public Ingredient getForm() {
//		return new Ingredient();
//	}
//	
//	@GetMapping
//	public String show(Model model) {
//		model.addAttribute("ingredients", service.findAll());
//		return "ingredient";
//	}
//	@GetMapping("/delete/{id}")
//	public String delete(@PathVariable Integer id) {
//		service.delete(id);
//		return "redirect:/admin/ingredient";
//	}
//	@GetMapping("/update/{id}")
//	public String update(@PathVariable Integer id, Model model) {
//		model.addAttribute("ingredient", service.findOne(id));
//		return show(model);
//	}
//	
//	@PostMapping
//	public String save(@ModelAttribute("ingredient") Ingredient ingredient,SessionStatus status) {
//		service.save(ingredient);
//		return cancel(status);
//		//return "redirect:/admin/ingredient";
//	}
//	@GetMapping("/cancel")
//	public String cancel(SessionStatus status) {
//		status.setComplete();
//		return "redirect:/admin/ingredient";
//	}
//}

package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


import ua.entity.Ingredient;
import ua.model.filter.SimpleFilter;
import ua.service.IngredientService;
import ua.validation.flag.CuisineFlag;
import ua.validation.flag.IngredientFlag;

@Controller
@RequestMapping("/admin/ingredient")
@SessionAttributes("ingredient")
public class AdminIngredientController {

	private final IngredientService service;
	
	@Autowired
	public AdminIngredientController(IngredientService service) {
		this.service = service;
	}
	
	@ModelAttribute("ingredient")
	public Ingredient getForm() {
		return new Ingredient();
	}
	@ModelAttribute("filter")
	public SimpleFilter getFilter() {
		return new SimpleFilter();
	}
	
	@GetMapping
	public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
		model.addAttribute("ingredients", service.findAll(pageable,filter));
		return "ingredient";
	}
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
		service.delete(id);
		return "redirect:/admin/ingredient"+buildParams(pageable, filter);
	}
	@GetMapping("/update/{id}")
	public String update(@PathVariable Integer id, Model model, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
		model.addAttribute("ingredient", service.findOne(id));
		return show(model, pageable, filter);
	}
	
	@PostMapping
	public String save(@ModelAttribute("ingredient") @Validated(IngredientFlag.class) Ingredient ingredient, BindingResult br, Model model, SessionStatus status, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
		if(br.hasErrors()) return show(model, pageable, filter);
		service.save(ingredient);
		return cancel(status,pageable,filter);
		//return "redirect:/admin/ingredient";
	}
	@GetMapping("/cancel")
	public String cancel(SessionStatus status,@PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
		status.setComplete();
		return "redirect:/admin/ingredient"+buildParams(pageable, filter);
	}
	private String buildParams(Pageable pageable, SimpleFilter filter) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("?page=");
		buffer.append(String.valueOf(pageable.getPageNumber()+1));
		buffer.append("&size=");
		buffer.append(String.valueOf(pageable.getPageSize()));
		if(pageable.getSort()!=null){
			buffer.append("&sort=");
			Sort sort = pageable.getSort();
			sort.forEach((order)->{
				buffer.append(order.getProperty());
				if(order.getDirection()!=Direction.ASC)
				buffer.append(",desc");
			});
		}
		buffer.append("&search=");
		buffer.append(filter.getSearch());
		return buffer.toString();
	}
}