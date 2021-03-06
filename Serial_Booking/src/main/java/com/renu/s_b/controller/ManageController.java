package com.renu.s_b.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.renu.s_b.models.Heading;
import com.renu.s_b.models.Notification;
import com.renu.s_b.models.PersonDetails;
import com.renu.s_b.models.Rules;
import com.renu.s_b.models.SerialBooking;
import com.renu.s_b.repositories.HeadingReapository;
import com.renu.s_b.repositories.NotificationRepository;
import com.renu.s_b.repositories.PersonDetailsRepository;
import com.renu.s_b.repositories.RulesRepository;
import com.renu.s_b.repositories.SerialBookingRepository;
import com.renu.s_b.utility.FileUploadUtility;
import com.renu.s_b.validator.ImageValidator;

@Controller
@ControllerAdvice
public class ManageController {
private static final Logger LOGGER=LoggerFactory.getLogger(ManageController.class);
@Autowired
PersonDetailsRepository personDetailsRepository;
@Autowired
RulesRepository rulesRepository;
@Autowired
NotificationRepository notificationRepository;
@Autowired
SerialBookingRepository serialBookingRepository;
@Autowired
HeadingReapository headingReapository;


@RequestMapping(value="/aassllaamm")
public String showManage(Model model) {
	LOGGER.info("From class ManageController,method : showManage()");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}
//GET PERSONDETAILS
@ModelAttribute("allpersondetails")
public List<PersonDetails>getAllPersonDetails(){
	List<PersonDetails>personDetails=personDetailsRepository.findAll();
	return personDetails;
	
}
//GET RULES
@ModelAttribute("rule")
public List<Rules>getAllRules(){
List<Rules>rules=rulesRepository.findAll();	
	
	return rules;
}

//GET NOTIFICATION
@ModelAttribute("notification")
public List<Notification>getAllNotification(){
List<Notification>notifications=notificationRepository.findAll();	
	
	return notifications;
}

//GET HEADING
@ModelAttribute("heading")
public List<Heading>getHeading(){
List<Heading>headings=headingReapository.findAll();	
	
	return headings;
}






//FOR PERSONDETAILS ADD
@RequestMapping(value="/addPersonDetails",method=RequestMethod.POST)
public String showManageForPersonDetals(@Valid @ModelAttribute("persondetails") PersonDetails personDetails,
		BindingResult bindingResult,HttpServletRequest httpServletRequest,Model model) {
	LOGGER.info("From class ManageController,method : showManageForPersonDetails()");
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	//file validation must be before binding result
	if (personDetails.getId()==null) {
		
		new ImageValidator().validate(personDetails,bindingResult);
	}
	if (bindingResult.hasErrors()) {
		model.addAttribute("message","Your operation has not been completed successfully !!!");
		return "manage";
	}
	if (personDetails.getId()==null) {
		personDetailsRepository.save(personDetails);
		personDetails.setId(null);
	}else {
		personDetailsRepository.save(personDetails);
	}
	if (!personDetails.getiFile().getOriginalFilename().equals("")) {
		FileUploadUtility.imageUploadFile(httpServletRequest, personDetails.getiFile(),personDetails.getiCode());
		
	}
	
	model.addAttribute("message","Your operation has been completed successfully !!!");
	return "manage";
}
//FOR PERSONDETAILS UPDATE
@RequestMapping(value="/updatePersonDetails")
public String updatePersonDetails(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : updatePersonDetails()");
    PersonDetails personDetails=personDetailsRepository.getById(id);
    model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
    model.addAttribute("persondetails", personDetails);
    model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	
	return "manage";
}
//FOR PERSONDETAILS DELETE
@RequestMapping(value="/personDetailsDeleteByAdmin")
public String deletePersonDetails(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : deletePersonDetails()");
	PersonDetails personDetails=personDetailsRepository.getById(id);
	String iCode=personDetails.getiCode();
	File iFile=new File(FileUploadUtility.IABS_PATH+iCode+".jpg");
	if (iFile.exists()) {
		iFile.delete();
	}
    personDetailsRepository.deleteById(id);;
	model.addAttribute("message","Id  "+id+"  has been deleted successfully !!!");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}
//FOR RULES
//ADD RULES
@RequestMapping(value="/addRules",method=RequestMethod.POST)
public String addRules(@Valid @ModelAttribute("rules") Rules rules,BindingResult bindingResult,Model model) {
	LOGGER.info("From class ManageController,method : addRules()");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	if (bindingResult.hasErrors()) {
		model.addAttribute("message","Your operation has not been completed successfully !!!");
		return "manage";
	}
	rulesRepository.save(rules);
	model.addAttribute("message","Your operation has been completed successfully !!!");
	return "manage";
}

//FOR RULES UPDATE
@RequestMapping(value="/updateRules")
public String updateRules(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : updateRules()");
  Rules rules=rulesRepository.getById(id);
  model.addAttribute("rules",rules);
	model.addAttribute("notifications",new Notification());
	
  model.addAttribute("persondetails", new PersonDetails());
  model.addAttribute("headings",new Heading());
  model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	
	return "manage";
}
//FOR RULES DELETE
@RequestMapping(value="/rulesDeleteByAdmin")
public String deleteRules(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : deleteRules()");
	Rules rules=rulesRepository.getById(id);
	
  rulesRepository.delete(rules);
	model.addAttribute("message","Id  "+id+"  has been deleted successfully !!!");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}

//FOR NOTIFICATION
//ADD NOTIFICATION
@RequestMapping(value="/addNotification",method=RequestMethod.POST)
public String addNotification(@Valid @ModelAttribute("notifications") Notification notification,BindingResult bindingResult,Model model) {
	LOGGER.info("From class ManageController,method : addNotification()");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	if (bindingResult.hasErrors()) {
		model.addAttribute("message","Your operation has not been completed successfully !!!");
		return "manage";
	}
	notificationRepository.save(notification);
	model.addAttribute("message","Your operation has been completed successfully !!!");
	return "manage";
}


//FOR NOTIFICATION UPDATE
@RequestMapping(value="/updateNotification")
public String updateNotification(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : updateNotification()");
Notification notification=notificationRepository.getById(id);
model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",notification);
	
model.addAttribute("persondetails", new PersonDetails());
model.addAttribute("headings",new Heading());
model.addAttribute("jsonurlPD","/getAllPersonDetails");
model.addAttribute("jsonurlR","/getAllRules");
model.addAttribute("jsonurlN","/getAllNotification");
model.addAttribute("jsonurlSMBD","/getAllSerial");
model.addAttribute("jsonurlH","/getHeading");
	
	return "manage";
}
//FOR NOTIFICATION DELETE
@RequestMapping(value="/notificationDeleteByAdmin")
public String deleteNotification(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : deleteNotification()");
	Notification notification=notificationRepository.getById(id);
	
notificationRepository.delete(notification);
	model.addAttribute("message","Id  "+id+"  has been deleted successfully !!!");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}



//FOR SERIAL BOOKING
//ADD SERIAL BOOKING
@RequestMapping(value="/addSerialBooking",method=RequestMethod.POST)
public String addSerialBooking(@Valid @ModelAttribute("serialbooking")SerialBooking serialBooking,BindingResult bindingResult,Model model) {
	LOGGER.info("From class ManageController,method : addSerialBooking()");
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlS","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	if (bindingResult.hasErrors()) {
		model.addAttribute("message","Your operation has not been completed successfully !!!");
		return "home";
	}
	//check contact already exist or not
	SerialBooking sBooking=serialBookingRepository.getByContact(serialBooking.getContact());
	if (sBooking!=null) {
		model.addAttribute("message","This contact number is already exist !!!");
		serialBooking.setId(null);
		return "home";
	}

	serialBookingRepository.save(serialBooking);
	serialBooking.setId(null);
	model.addAttribute("message","Your operation has been completed successfully !!!");
	return "home";
}
//FOR SERIAL DELETE
@RequestMapping(value="/serialDeleteByAdmin")
public String deleteSerial(Model model) {
	LOGGER.info("From class ManageController,method : deleteSerial()");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	serialBookingRepository.deleteAll();
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlS","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}



//FOR UPDATING 
@RequestMapping(value="/updateSerial")
public String getContact(@RequestParam("id") Long id,Model model) {
	LOGGER.info("From class ManageController,method : getContact()");
    model.addAttribute("id",id);
    return "showPreUpdate";
	
}

//FOR UPDATE SERIAL BOOKING
@RequestMapping(value="/sendContact")
public String getUpdateForm(@RequestParam("id") Long id,@RequestParam("contact")String contact,Model model) {
	LOGGER.info("From class ManageController,method : getUpdateForm()");
	SerialBooking serialBookingById=serialBookingRepository.getById(id);
	String contactById=serialBookingById.getContact();
	SerialBooking serialBookingByContact=serialBookingRepository.getByContact(contact);
	if (serialBookingByContact==null) {
		model.addAttribute("id",id);
    	model.addAttribute("message","Your contact is wrong !!!");
    	return "showPreUpdate";
    }
    
	String contactByContact=serialBookingByContact.getContact();
    if (contactById==contactByContact) {
	model.addAttribute("serialbooking",serialBookingById);
	model.addAttribute("jsonurlS","/getAllSerial");
	return "home";
	}else {
		model.addAttribute("message","Your contact is wrong !!!");
		return "showPreUpdate";
	}
    
	
}

//FOR SERIAL MANAGE DELETE BY ADMIN
@RequestMapping(value="/serialManageDeleteByAdmin")
public String serialManageDelete(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : serialManageDelete()");
	SerialBooking serialBooking=serialBookingRepository.getById(id);
	
	model.addAttribute("message","Id  "+id+"  has been deleted successfully !!!");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings", new  Heading());
	serialBookingRepository.delete(serialBooking);
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}

//FOR HEADING
//ADD HEADING
@RequestMapping(value="/addHeading",method=RequestMethod.POST)
public String addHeading(@Valid @ModelAttribute("headings") Heading heading,BindingResult bindingResult,Model model) {
	LOGGER.info("From class ManageController,method : addHeading()");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	if (bindingResult.hasErrors()) {
		model.addAttribute("message","Your operation has not been completed successfully !!!");
		return "manage";
	}
	headingReapository.save(heading);
	model.addAttribute("message","Your operation has been completed successfully !!!");
	return "manage";
}

//FOR HEADING UPDATE
@RequestMapping(value="/updateHeading")
public String updateHeading(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : updateHeading()");
Heading heading=headingReapository.getById(id);
model.addAttribute("rules",new Rules());
model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",heading);
	
model.addAttribute("persondetails", new PersonDetails());
model.addAttribute("jsonurlPD","/getAllPersonDetails");
model.addAttribute("jsonurlR","/getAllRules");
model.addAttribute("jsonurlN","/getAllNotification");
model.addAttribute("jsonurlSMBD","/getAllSerial");
model.addAttribute("jsonurlH","/getHeading");
	
	return "manage";
}



//FOR HEADING DELETE
@RequestMapping(value="/headingDeleteByAdmin")
public String deleteHeading(@RequestParam("id")Long id,Model model) {
	LOGGER.info("From class ManageController,method : deleteHeading()");
	Heading heading=headingReapository.getById(id);
	
headingReapository.delete(heading);
	model.addAttribute("message","Id  "+id+"  has been deleted successfully !!!");
	model.addAttribute("persondetails",new PersonDetails());
	model.addAttribute("rules",new Rules());
	model.addAttribute("notifications",new Notification());
	model.addAttribute("headings",new Heading());
	model.addAttribute("jsonurlPD","/getAllPersonDetails");
	model.addAttribute("jsonurlR","/getAllRules");
	model.addAttribute("jsonurlN","/getAllNotification");
	model.addAttribute("jsonurlSMBD","/getAllSerial");
	model.addAttribute("jsonurlH","/getHeading");
	return "manage";
}

}
