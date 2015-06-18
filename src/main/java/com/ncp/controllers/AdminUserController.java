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

import com.ncp.entity.User;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.UserRepository;
import com.ncp.service.PitchbookReaderService;


@Controller
@RequestMapping("/admin/user/*")
public class AdminUserController {
	
	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private PitchbookReaderService pitchbookReaderService;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("user.index", model);
	}
	
	@RequestMapping(value="loadUserTable")
	public @ResponseBody DatatablesForm loadUserTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<User> categories = userRepository.findAll();
		
		for(User user : categories){
			String password = "";
			for(int i=0; i<user.getEmailPassword().length(); i++){
				password+= "&bull;";
			}
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(user.getId().toString());
			curRow.add(user.getUsername());
			curRow.add(user.getPassword());
			curRow.add(user.getFirstName());
			curRow.add(user.getLastName());
			curRow.add(user.getEmailAddress());
			curRow.add(password);
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}
	
	@RequestMapping(value="saveNewUser")
	public ResponseEntity<String> saveNewUser(@RequestParam String username, @RequestParam String password,
			@RequestParam String emailAddress, @RequestParam String emailPassword, @RequestParam String firstName,
			@RequestParam String lastName) throws Exception{
		try{
			User user = new User();
			user.setUsername(username);
			user.setPassword(emailPassword);
			user.setEmailAddress(emailAddress);
			user.setEmailPassword(emailPassword);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setCreatedBy("NCP");
			user.setCreatedDate(DateTime.now());
			user.setLastModifiedBy("NCP");
			user.setLastModifiedDate(DateTime.now());
			user.setVersion(0L);
			
			userRepository.save(user);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="runConversionForUser")
	public ResponseEntity<String> runConversionForUser(@RequestParam Long userId) throws Exception{
		try{
			User user = userRepository.findOne(userId);

			pitchbookReaderService.scanUserEmailAccount(user);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
}
