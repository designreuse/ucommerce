package com.fursel.tenant.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fursel.persistence.Category;
import com.fursel.persistence.service.CategoryService;
import com.fursel.tenant.domain.CategoryForm;
import com.fursel.tenant.domain.PageWrapper;

@Controller
@RequestMapping("/categories")
public class CategoryController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String landing(Model uiModel, Pageable pageable, @RequestParam("name") String name, @RequestParam("desc") String description) {
		PageWrapper<Category> page = new PageWrapper<Category> (categoryService.getCategories(pageable, name, description), "/categories");
		uiModel.addAttribute("page", page);
		return "/category";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("category") CategoryForm category, BindingResult result, RedirectAttributes redirectAttrs){
		if (result.hasErrors()) {
			return "redirect:/categories";
		}
		categoryService.addCategory(category.toEntity());
		redirectAttrs.addFlashAttribute("message", "Category Created !");
		return "redirect:/categories";
	}
	
	@ModelAttribute("categories")
	private CategoryForm getCategory() {
		return new CategoryForm();
	}
}
