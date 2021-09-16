/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.bahmnisyncmaster.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.GlobalProperty;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmnisyncmaster.BahmniSyncMasterLog;
import org.openmrs.module.bahmnisyncmaster.BahmniSyncMasterLogDTO;
import org.openmrs.module.bahmnisyncmaster.service.DataPushService;
import org.openmrs.module.bahmnisyncmaster.util.BahmniSyncMasterConstants;
import org.openmrs.web.WebConstants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/basicmodule/basicmoduleLink.form'.
 */
@Controller
public class MasterConflict2Controller {
	
	@Autowired
	DataPushService dataPullService;
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/** Success form view name */
	private final String SUCCESS_FORM_VIEW = "/module/bahmnisyncmaster/conflict2";
	
	/**
	 * Initially called after the formBackingObject method to get the landing form name
	 * 
	 * @return String form view name
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/module/bahmnisyncmaster/conflict2.form")
	public String showForm() {
		return SUCCESS_FORM_VIEW;
	}
	
	@ModelAttribute("mastersyncconflicts")
	public String getModel() throws JsonGenerationException, JsonMappingException, IOException {
		
		List<BahmniSyncMasterLogDTO> logs = dataPullService.getManipulatedConflictsBahmniSyncMasterLog();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(logs);
				
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/module/bahmnisyncmaster/resolveConflict.form")
	public @ResponseBody String markConflictAsResolved(@RequestParam("id")Integer id) throws JsonGenerationException, JsonMappingException, IOException
	{
		dataPullService.markLogAsResolved(id);
		return "success";
	}

}
