package com.ncp.service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SearchTerm;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncp.entity.Category;
import com.ncp.entity.Company;
import com.ncp.entity.CompanyRecordLink;
import com.ncp.entity.CompanyRecordType;
import com.ncp.entity.Industry;
import com.ncp.entity.Location;
import com.ncp.entity.PitchbookRecord;
import com.ncp.entity.User;
import com.ncp.repositories.CategoryRepository;
import com.ncp.repositories.CompanyRecordLinkRepository;
import com.ncp.repositories.CompanyRecordTypeRepository;
import com.ncp.repositories.CompanyRepository;
import com.ncp.repositories.IndustryRepository;
import com.ncp.repositories.LocationRepository;
import com.ncp.repositories.PitchbookRecordRepository;

@Service
public class PitchbookReaderService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PitchbookRecordRepository pitchbookRecordRepository;
	
	@Autowired
	IndustryRepository industryRepository;
	
	@Autowired
	CompanyRecordTypeRepository companyRecordTypeRepository;
	
	@Autowired
	CompanyRecordLinkRepository companyRecordLinkRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	
	

	private static final Pattern REMOVE_EMPTY_SPACES = Pattern.compile("^\\s+$");
	private static final Pattern REMOVE_SPACES = Pattern.compile("&nbsp;");
	private static final Pattern BAD_WORDS = Pattern.compile("^((VIEW|VIEW DETAILS))$");
	private static String BLOCK_SEPARATER = "SHARE:";
	private static final Pattern digit = Pattern.compile("[0-9|\\.]");
	private static final Pattern word = Pattern.compile("(a-z|A-Z)");
	private static final Pattern MONEY = Pattern.compile("^(\\$|£|¥|$NIS|€)[0-9|\\.]* \\w");
	private static final Pattern USAlocation = Pattern.compile(".*, (AL|AK|AR|AZ|CA|CO|CT|DC|DE|FL|GA|HI|IA|ID|IL|IN|KS|KY|LA|MA|MD|ME|MI|MN|MO|MS|MT|NC|ND|NE|NH|NJ|NM|NV|NY|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VA|VT|WA|WI|WV|WY|D\\.C\\.)$");
	private List<String> globalLocations = new ArrayList<String>();
	List<CompanyRecordLink> newCompanyRecordLinks = new ArrayList<CompanyRecordLink>();
	
	public void scanUserEmailAccount(User user) throws Exception{
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		  
		DateTime dateLastRan = user.getDateLastRan();
		if(dateLastRan==null)
			dateLastRan = DateTime.now().minusYears(100);
		final DateTime finalDate = dateLastRan;
		//create session, store and folder
	    Session session = Session.getInstance(props, null);
		Store store = session.getStore();
		store.connect("imap.gmail.com", user.getEmailAddress(), user.getEmailPassword());
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);
		//create search criteria
		SearchTerm searchCondition = new SearchTerm() {
		    public boolean match(Message message) {
		        try {
		        	Address[] fromAddress = message.getFrom();
		            if (fromAddress != null && fromAddress.length > 0) {
		                if (fromAddress[0].toString().contains("PitchBook PE & VC News") && message.getSentDate().after(finalDate.toDate())) {
		                    return true;
		                }
		            }
		        } catch (MessagingException ex) {
		            ex.printStackTrace();
		        }
		        return false;
		    }
		};
		
		// performs search through the folder
        Message[] messages = inbox.search(searchCondition);
		
        if(messages.length<=0){
	      throw new Exception("Did not find any emails from pitchbook newsletter on email server.");
	    }
	        
	    //create array of string to hold separate lines of string 
	    List<String[]> lines = new ArrayList<String[]>();
	    HashMap<Date, List<String>> messageLines = new HashMap<Date, List<String>>();
	    List<Date> validDates = new ArrayList<Date>();
	    List<String> companySection = new ArrayList<String>();
	    //loop through message to get each line 
	    for(Message msg : messages){
	    	Object content = msg.getContent();  
	     	if (content instanceof String)  
	       	{
	       		//Get body in string and split the message into string of 
	       		//arrays based on carraige returns 
	       		//add these rows to the lines array to hold all lines 
	       	    if(!messageLines.containsKey(msg.getReceivedDate())){
	       	    	validDates.add(msg.getReceivedDate());
	       	    	String body = (String)content; 
		       	    String[] templines = body.split("<.+?>");
		       	    String[] companySplit = body.split("in the News");
		       	    companySection.add(companySplit[1]);
		       	    lines.add(templines);
		       	    messageLines.put(msg.getReceivedDate(), Arrays.asList(body.split("SHARE:")));
	       	    }
	     		
	       	}
	       	else if (content instanceof Multipart)  
	       	{
	       		//currently not used
	       	    Multipart mp = (Multipart)content;  
	       	    MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(0);
		        BodyPart bp = mp.getBodyPart(0); 
	       	}
	    }
		
	    analyzeForCompanies(companySection);
	    List<Company> validCompanies = companyRepository.findAll();
	    HashMap<String, Company> companyHash = new HashMap<String, Company>();
	    for(Company company : validCompanies){
	    	companyHash.put(company.getName(), company);
	    }
	    List<Category> validCategories = categoryRepository.findAll();
	    HashMap<String, Category> categoryHash = new HashMap<String, Category>();
	    for(Category category : validCategories){
	    	categoryHash.put(category.getCategory(), category);
	    }
	    List<CompanyRecordType> validCompanyRecordTypes = companyRecordTypeRepository.findAll();
	    HashMap<String, CompanyRecordType> typeHash = new HashMap<String, CompanyRecordType>();
	    for(CompanyRecordType type : validCompanyRecordTypes){
	    	typeHash.put(type.getCompanyRecordType().toLowerCase(), type);
	    	typeHash.put(type.getCompanyRecordType().toLowerCase()+"s", type);
	    }
	    List<Industry> industryTypes = industryRepository.findAll();
	    HashMap<String, Industry> industryHash = new HashMap<String, Industry>();
	    for(Industry industry : industryTypes){
	    	industryHash.put(industry.getIndustry(), industry);
	    }
	    
	    HashMap<Date, List<String>> validMessageBlocks = new HashMap<Date, List<String>>();
	    for(Date date : validDates){
	    	validMessageBlocks.put(date, getBlocks(messageLines.get(date)));
	    }
	    
	    
	    Iterator<Map.Entry<Date, List<String>>> iterator = validMessageBlocks.entrySet().iterator();
	    List<PitchbookRecord> newRecords = new ArrayList<PitchbookRecord>();
        while(iterator.hasNext()){
            Map.Entry<Date, List<String>> entry = iterator.next();
            Date recordDate = entry.getKey();
            for(String line : entry.getValue()){
	    		boolean hasCategory = checkForACategory(line);
	    		if(hasCategory){
	    			List<String> finalBlock = new ArrayList<String>();
	    			boolean lookForEnd = false;
	    			int tableEndFound = 0;
	    			for(String l : line.split("\n|\r|\t")){
		    			if (l.trim().length() > 0 && !l.matches("")){
		    				if(lookForEnd){
		    					if(tableEndFound>1){
		    						break;
		    					}
		    					else{
		    						if(l.contains("TABLE END")){
		    							tableEndFound++;
		    						}
		    						else{
		    							finalBlock.add(l.trim());
		    						}
		    					}
		    				}
		    				else{
		    					if(l.contains("&raquo;")){
			    	    			lookForEnd = true;
			    	    		}
			    	    		else{
			    	    			finalBlock.add(l.trim());
			    	    		}
		    				}
		    			}
		    		}
	    			PitchbookRecord newRecord = anaylzeBlock(recordDate, finalBlock, categoryHash, industryHash, companyHash, typeHash);
	    			if(newRecord != null && newRecord.getCategory()!=null)
	    				newRecords.add(newRecord);
	    		}
            }
            iterator.remove(); // right way to remove entries from Map, 
                               // avoids ConcurrentModificationException
        }
        try{
        	industryRepository.save(industryHash.values());
			companyRepository.save(companyHash.values());
        	pitchbookRecordRepository.save(newRecords);
            companyRecordLinkRepository.save(newCompanyRecordLinks);
        }
        catch(Exception e){
        	System.out.println(e.getMessage());
        }
	}
	
	private PitchbookRecord anaylzeBlock(Date date, List<String> block, HashMap<String, Category> categories
			, HashMap<String, Industry> industries, HashMap<String, Company> companies
			,  HashMap<String, CompanyRecordType> companyRecordTypes){
		PitchbookRecord record = new PitchbookRecord();
		List<CompanyRecordLink> tempLinks = new ArrayList<CompanyRecordLink>();
		try{
		
		boolean gettingToDesc = true;
		boolean analyzingDesc = false;
		boolean gettingMetaData = false;
		String companyTypeKey = "";
		for(String line : block){
			if(gettingToDesc){
				if(!line.contains("TABLE")){
					gettingToDesc = false;
					analyzingDesc = true;
					record.setDescription(record.getDescription() == null ? line : record.getDescription() + " " + line);
				}
			}
			else if(analyzingDesc){
				if(line.contains("TABLE")){
					analyzingDesc = false;
					gettingMetaData = true;
				}
				else{
					record.setDescription(record.getDescription() == null ? line : record.getDescription() + " " + line);
				}
			}
			else if(gettingMetaData){
				if(line.contains("TABLE")){
					gettingMetaData = false;
				}
				else if(companies.keySet().contains(line.trim())){
					CompanyRecordLink mainCompanyLink = new CompanyRecordLink();
					mainCompanyLink.setCompany(companies.get(line.trim()));
					mainCompanyLink.setCompanyRecordType(companyRecordTypes.get("main company"));
					mainCompanyLink.setPitchbookRecord(record);
					mainCompanyLink.setCreatedBy("NCP");
					mainCompanyLink.setCreatedDate(DateTime.now());
					mainCompanyLink.setLastModifiedBy("NCP");
					mainCompanyLink.setLastModifiedDate(DateTime.now());
					mainCompanyLink.setVersion(0L);
					tempLinks.add(mainCompanyLink);
				}
				else if(categories.keySet().contains(line.trim())){
					record.setCategory(categories.get(line.trim()));
				}
				else if(MONEY.matcher(line).find()){
					if(line.contains("$"))
						record.setMonetaryUnit("$");
					else if(line.contains("£"))
						record.setMonetaryUnit("£");
					else if(line.contains("¥"))
						record.setMonetaryUnit("¥");
					else if(line.contains("NIS"))
						record.setMonetaryUnit("NIS");
					else if(line.contains("€"))
						record.setMonetaryUnit("€");
					
					line.replaceAll("$|£|¥|NIS|€", "");
					String finalMoney = "";
					String metaMoney = "";
					for(int i=0; i<line.length(); i++){
						if(Character.isLetter(line.charAt(i))){
							metaMoney += line.charAt(i);
						}
						else if(Character.isDigit(line.charAt(i))){
							finalMoney += line.charAt(i);
						}
						else if(line.charAt(i) == '.'){
							finalMoney += line.charAt(i);
						}
					}
					
					Float money = Float.valueOf(finalMoney);
					if(metaMoney.contains("million") || metaMoney.contains("Million")){
						money = money*1000000;
					}
					else if(metaMoney.contains("billion") || metaMoney.contains("Billion")){
						money = money*1000000000;
					}
					else if(metaMoney.contains("thousand") || metaMoney.contains("Thousand")){
						money = money*1000;
					}
					else if(metaMoney.contains("hundren") || metaMoney.contains("Hundred")){
						money = money*100;
					}
					record.setMonetaryValue((float)money);
				}
				else if((USAlocation.matcher(line).find())){
					String[] usa = line.split(",");
					String city = usa[0].trim();
					String state = usa[1].trim();
					String country = "united states";
					record.setCity(city);
					record.setState(state);
					record.setCountry(country);
				}
				else if(industries.keySet().contains(line.trim())){
					record.setIndustry(industries.get(line.trim()));
				}
				else{
					if((line.contains(",") && line.split(",").length > 1)){
						String[] loc = line.split(",");
						String city = loc[0].trim().toLowerCase();
						String state = "";
						String country = loc[1].trim().toLowerCase();
						record.setCity(city);
						record.setState(state);
						record.setCountry(country);
					}
					else if(!line.contains("TABLE") && record.getIndustry() == null){
					  Industry industry = new Industry();
					  industry.setIndustry(line.trim());
					  industry.setCreatedBy("NCP");
					  industry.setCreatedDate(DateTime.now());
					  industry.setLastModifiedBy("NCP");
					  industry.setLastModifiedDate(DateTime.now());
					  industry.setVersion(0L);
					  record.setIndustry(industry);
					  industries.put(line.trim(), industry);
					}
				}
			}
			else{
				if(!(line.contains("TABLE") || line.contains("&raquo;"))){
					if(companyRecordTypes.keySet().contains(line.trim().toLowerCase())){
						companyTypeKey = line.trim().toLowerCase();
					}
					else if(!companyTypeKey.equals("") && companyRecordTypes.containsKey(companyTypeKey)){
						CompanyRecordLink newLink = new CompanyRecordLink();
						newLink.setCompanyRecordType(companyRecordTypes.get(companyTypeKey));
						newLink.setPitchbookRecord(record);
						if(companies.containsKey(line.trim())){
							newLink.setCompany(companies.get(line.trim()));
						}
						else{
							Company company = new Company();
							company.setName(line.trim());
							company.setCreatedBy("NCP");
							company.setCreatedDate(DateTime.now());
							company.setLastModifiedBy("NCP");
							company.setLastModifiedDate(DateTime.now());
							company.setVersion(0L);
							companies.put(line.trim(), company);
							newLink.setCompany(company);
						}
						newLink.setCreatedBy("NCP");
						newLink.setCreatedDate(DateTime.now());
						newLink.setLastModifiedBy("NCP");
						newLink.setLastModifiedDate(DateTime.now());
						newLink.setVersion(0L);
						tempLinks.add(newLink);
					}
				}
			}
		}
		record.setRecordDate(new DateTime(date));
		record.setCreatedBy("NCP");
		record.setCreatedDate(DateTime.now());
		record.setLastModifiedBy("NCP");
		record.setLastModifiedDate(DateTime.now());
		record.setVersion(0L);
		
			
		if(record.getCategory()!=null)
			newCompanyRecordLinks.addAll(tempLinks);
		return record;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
		
	private boolean checkForACategory(String line){
		for(String category : categoryRepository.findAllCategoryNames()){
			if(line.contains(category)){
				return true;
			}
		}
		return false;
	}
	
	private List<String> getBlocks(List<String> message) throws Exception{
		List<String> blocks = new ArrayList<String>();
		for(String text : message){
			if(text.contains("VIEW DETAILS")){
				text = text.replace("&nbsp;", " ");
				text = text.replace("&amp;", "&");
				text = text.replace("VIEW DETAILS", "");
				text = text.replace("VIEW", "");
				text = text.replaceAll("<table", " TABLE BEGIN <");
				text = text.replaceAll("</table>", " TABLE END ");
				text = text.replaceAll("<.+?>", "\n");
				text = text.replaceAll("^\\s+$", "\n");
				
				blocks.add(text);
			}
		}
		return blocks;
	}
	
	private void analyzeForCompanies(List<String> messages) throws Exception{
		List<String> keywords = new ArrayList<String>();
		keywords.add("Investors");
		keywords.add("Companies");
		keywords.add("Service Providers");
		keywords.add("?");
		boolean stopAnalyzing = false;
		List<String> currentCompanyNames = companyRepository.findAllCompanyNames();
		List<String> tcompanies = new ArrayList<String>();
		for(String message : messages){
			for(String line : message.split("<br />|<.+?>")){
				line = line.replaceAll("\t|\n|\r", "");
				line = line.replace("&amp;", "&");
			  if(!line.trim().equals("") && !REMOVE_EMPTY_SPACES.matcher(line.trim()).find() && 
					  !line.trim().equals("?") && !tcompanies.contains(line.trim()) && 
					  !currentCompanyNames.contains(line.trim()) && !keywords.contains(line.trim()) && !stopAnalyzing){
				  if(line.trim().equals("People") || line.trim().contains("Pitchbook")){
					  stopAnalyzing = true;
				  }
				  else{
					  tcompanies.add(line.trim());
				  }
			  }
		  }
		}
		List<Company> newCompanies = new ArrayList<Company>();
		for(String companyName : tcompanies){
			Company company = new Company();
			company.setName(companyName);
			company.setCreatedBy("NCP");
			company.setCreatedDate(DateTime.now());
			company.setLastModifiedBy("NCP");
			company.setLastModifiedDate(DateTime.now());
			company.setVersion(0L);
			currentCompanyNames.add(companyName);
			newCompanies.add(company);
		}
		
		companyRepository.save(newCompanies);
	  }
	
	
	private void getLocations() throws Exception{
		  try{
			  
			  //Connection con = Jsoup.connect("http://google.com/search?q=" + webString).userAgent("Mozilla").timeout(60000);
			  Connection con = Jsoup.connect("http://www.state.gov/s/inr/rls/4250.htm").userAgent("Mozilla").timeout(60000);
			  Connection.Response resp = con.execute();
			  Document doc = null;
		      if (resp.statusCode() == 200) {
		          doc = con.get();
		          
		          Element table = doc.select("table").first();

		          Iterator<Element> ite = table.select("td").iterator();
		          
		          while(ite.hasNext()){	        	  
		        		  String l = ite.next().text();
		        		  
		        		  l = l.replace("*", "");
		        		  l = l.replace("+", "");
		        		  l = l.replaceAll("\\((.+?)\\)", "");
		        		  globalLocations.add(l.trim());  
		          }
		      }
		  }
		  catch(Exception e){
			  System.out.println(e.getMessage());
		  }
	  }
	
}
