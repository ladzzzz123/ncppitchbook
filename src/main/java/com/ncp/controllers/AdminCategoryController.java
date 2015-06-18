package com.ncp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ncp.entity.Category;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.CategoryRepository;


@Controller
@RequestMapping("/admin/category/*")
public class AdminCategoryController {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("category.index", model);
	}
	
	@RequestMapping(value="loadCategoryTable")
	public @ResponseBody DatatablesForm loadRecordsTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<Category> categories = categoryRepository.findAll();
		
		for(Category category : categories){
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(category.getId().toString());
			curRow.add(category.getCategory());
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}
	
	@RequestMapping(value="saveNewCategory")
	public ResponseEntity<String> saveNewCategory(@RequestParam String category) throws Exception{
		try{
			Category cat = new Category();
			cat.setCategory(category);
			cat.setCreatedBy("NCP");
			cat.setCreatedDate(DateTime.now());
			cat.setLastModifiedBy("NCP");
			cat.setLastModifiedDate(DateTime.now());
			cat.setVersion(0L);
			
			categoryRepository.save(cat);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
}
