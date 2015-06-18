package com.ncp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ncp.entity.CompanyRecordLink;
import com.ncp.entity.CompanyRecordType;
import com.ncp.entity.PitchbookRecord;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.CategoryRepository;
import com.ncp.repositories.CompanyRecordLinkRepository;
import com.ncp.repositories.CompanyRecordTypeRepository;
import com.ncp.repositories.CompanyRepository;
import com.ncp.repositories.IndustryRepository;
import com.ncp.repositories.PitchbookRecordRepository;


@Controller
@RequestMapping("/records/*")
public class PitchbookRecordsController {
	
	@Autowired
	private PitchbookRecordRepository pitchbookRecordRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	IndustryRepository industryRepository;
	
	@Autowired
	CompanyRecordTypeRepository companyRecordTypeRepository;
	
	@Autowired
	CompanyRecordLinkRepository companyRecordLinkRepository;
	
	@Autowired
	CategoryRepository categoryRepository;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		List<String> types = companyRecordTypeRepository.findAllCompanyRecordTypesOrderByType();
		model.put("types", types);
		return new ModelAndView("records.index", model);
	}
	
	@RequestMapping(value="loadRecordsTable")
	public @ResponseBody DatatablesForm loadRecordsTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<PitchbookRecord> pitchbookRecords = pitchbookRecordRepository.findAll();
		List<String> types = companyRecordTypeRepository.findAllCompanyRecordTypesOrderByType();
		
		for(PitchbookRecord record : pitchbookRecords){
			HashMap<String, String> typeStrings = new HashMap<String, String>();
			for(String type : types){
				typeStrings.put(type, "");
			}
			for(CompanyRecordLink link : record.getCompanyRecordLinks()){
				typeStrings.put(link.getCompanyRecordType().getCompanyRecordType(), typeStrings.get(link.getCompanyRecordType().getCompanyRecordType()).equals("")?link.getCompany().getName():typeStrings.get(link.getCompanyRecordType().getCompanyRecordType())+", " + link.getCompany().getName());
			}
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(record.getId().toString());
			curRow.add(record.getCategory()==null?"":record.getCategory().getCategory());
			curRow.add(record.getIndustry()==null?"":record.getIndustry().getIndustry());
			curRow.add(typeStrings.get("Main Company"));
			curRow.add(record.getMonetaryUnit()==null?"":record.getMonetaryUnit() + " " + record.getMonetaryValue()==null?"":String.valueOf((long)record.getMonetaryValue()));
			curRow.add(record.getCity()==null?"":record.getCity());
			curRow.add(record.getState()==null?"":record.getState());
			curRow.add(record.getCountry()==null?"":record.getCountry());
			curRow.add(record.getDescription()==null?"":record.getDescription());
			for(String type : types){
				if(!type.equals("Main Company"))
					curRow.add(typeStrings.get(type));
			}
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}
	
}
