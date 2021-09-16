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
package org.openmrs.module.bahmnisyncworker.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmnisyncworker.util.BahmniSyncWorkerConstants;
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
@RequestMapping(value = "module/bahmnisyncworker/config.form")
public class WorkerConfigController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/** Success form view name */
	private final String SUCCESS_FORM_VIEW = "/module/bahmnisyncworker/config";
	
	/**
	 * Initially called after the formBackingObject method to get the landing form name
	 * 
	 * @return String form view name
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showForm() {
		return SUCCESS_FORM_VIEW;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String handleSubmission(@ModelAttribute("globalPropertiesModel") GlobalPropertiesModel globalPropertiesModel,
	        Errors errors, WebRequest request) {
		globalPropertiesModel.validate(globalPropertiesModel, errors);
		if (errors.hasErrors())
			return null; // show the form again
			
		AdministrationService administrationService = Context.getAdministrationService();
		for (GlobalProperty p : globalPropertiesModel.getProperties()) {
			administrationService.saveGlobalProperty(p);
		}
		
		request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage("general.saved"),
		    RequestAttributes.SCOPE_SESSION);
		return SUCCESS_FORM_VIEW;
	}
	
	/**
	 * @return
	 */
	@ModelAttribute("globalPropertiesModel")
	public GlobalPropertiesModel getModel() {
		List<GlobalProperty> editableProps = new ArrayList<GlobalProperty>();
		
		Set<String> props = new LinkedHashSet<String>();
		props.add(BahmniSyncWorkerConstants.WORKER_NODE_ID_GLOBAL_PROPERTY_NAME);
		props.add(BahmniSyncWorkerConstants.MASTER_URL_GLOBAL_PROPERTY_NAME);
		props.add(BahmniSyncWorkerConstants.KAFKA_URL_GLOBAL_PROPERTY_NAME);
		props.add(BahmniSyncWorkerConstants.CHUNK_SIZE_GLOBAL_PROPERTY_NAME);
		props.add(BahmniSyncWorkerConstants.DATABASE_SERVER_NAME);
		props.add(BahmniSyncWorkerConstants.OPENMRS_SCHEME_NAME);
		props.add(BahmniSyncWorkerConstants.MASTER_NODE_USER);
		props.add(BahmniSyncWorkerConstants.MASTER_NODE_PASSWORD);
		props.add(BahmniSyncWorkerConstants.OPENMRS_SCHEME_NAME);
		
		//remove the properties we dont want to edit
		for (GlobalProperty gp : Context.getAdministrationService().getGlobalPropertiesByPrefix(
		    BahmniSyncWorkerConstants.MODULE_ID)) {
			if (props.contains(gp.getProperty()))
				editableProps.add(gp);
		}
		
		return new GlobalPropertiesModel(editableProps);
	}
	
	/**
	 * Represents the model object for the form, which is typically used as a wrapper for the list
	 * of global properties list so that spring can bind the properties of the objects in the list.
	 * Also capable of validating itself
	 */
	public class GlobalPropertiesModel implements Validator {
		
		private List<GlobalProperty> properties;
		
		public GlobalPropertiesModel() {
		}
		
		public GlobalPropertiesModel(List<GlobalProperty> properties) {
			this.properties = properties;
		}
		
		/**
		 * @see org.springframework.validation.Validator#supports(java.lang.Class)
		 */
		@Override
		public boolean supports(Class<?> clazz) {
			return clazz.equals(getClass());
		}
		
		/**
		 * @see org.springframework.validation.Validator#validate(java.lang.Object,
		 *      org.springframework.validation.Errors)
		 */
		@Override
		public void validate(Object target, Errors errors) {
			GlobalPropertiesModel model = (GlobalPropertiesModel) target;
			/*for (int i = 0; i < model.getProperties().size(); ++i) {
				GlobalProperty gp = model.getProperties().get(i);
				if (gp.getProperty().equals(BahmniSyncWorkerConstants.URI_PREFIX_GLOBAL_PROPERTY_NAME)) {
					// TODO validate legal uri prefix
				} else if (gp.getProperty().equals(BahmniSyncWorkerConstants.ALLOWED_IPS_GLOBAL_PROPERTY_NAME)) {
					// TODO validate legal comma-separated IPv4 or IPv6 addresses, wildcards, etc
				} else if (gp.getProperty().equals(BahmniSyncWorkerConstants.MAX_RESULTS_DEFAULT_GLOBAL_PROPERTY_NAME)) {
					boolean okay = false;
					try {
						Integer maxResultsAbsoluteVal = Integer.valueOf(model.getProperty(
						    BahmniSyncWorkerConstants.MAX_RESULTS_ABSOLUTE_GLOBAL_PROPERTY_NAME).getPropertyValue());
						if (Integer.valueOf(gp.getPropertyValue()) > 0
						        && Integer.valueOf(gp.getPropertyValue()) <= maxResultsAbsoluteVal) {
							okay = true;
						}
					}
					catch (Exception ex) {}
					if (!okay)
						errors.rejectValue("properties[" + i + "]", BahmniSyncWorkerConstants.MODULE_ID
						        + ".maxResultsDefault.errorMessage");
				} else if (gp.getProperty().equals(BahmniSyncWorkerConstants.MAX_RESULTS_ABSOLUTE_GLOBAL_PROPERTY_NAME)) {
					boolean okay = false;
					try {
						okay = Integer.valueOf(gp.getPropertyValue()) > 0;
					}
					catch (Exception ex) {}
					if (!okay)
						errors.rejectValue("properties[" + i + "]", BahmniSyncWorkerConstants.MODULE_ID
						        + ".maxResultsAbsolute.errorMessage");
				}
			}*/
		}
		
		/**
		 * Returns the global property for the given propertyName
		 * 
		 * @param propertyName
		 * @return
		 */
		public GlobalProperty getProperty(String propertyName) {
			GlobalProperty prop = null;
			for (GlobalProperty gp : getProperties()) {
				if (gp.getProperty().equals(propertyName)) {
					prop = gp;
					break;
				}
			}
			return prop;
		}
		
		/**
		 * @return
		 */
		public List<GlobalProperty> getProperties() {
			return properties;
		}
		
		/**
		 * @param properties
		 */
		public void setProperties(List<GlobalProperty> properties) {
			this.properties = properties;
		}
	}
	
}
