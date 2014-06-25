/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.mule.api.config.MuleProperties;
import org.mule.tck.junit4.FunctionalTestCase;

/**
 * This is the base test class for Templates integration tests.
 * 
 * @author damiansima
 * @author martin.zdila
 */
public abstract class AbstractTemplateTestCase extends FunctionalTestCase {
	
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static final String MULE_DEPLOY_PROPERTIES_PATH = "./src/main/app/mule-deploy.properties";

	@Override
	protected String getConfigResources() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(MULE_DEPLOY_PROPERTIES_PATH));
		} catch (IOException e) {
			throw new IllegalStateException(
					"Could not find mule-deploy.properties file on classpath." +
					"Please add any of those files or override the getConfigResources() method to provide the resources by your own.");
		}
		
		return props.getProperty("config.resources");
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());
		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, new File(MAPPINGS_FOLDER_PATH).getAbsolutePath());
		return properties;
	}

}
