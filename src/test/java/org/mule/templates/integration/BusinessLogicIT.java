/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.AbstractTemplateTestCase;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.db.MySQLDbCreator;
import org.mule.templates.utils.VariableNames;
import org.mule.util.UUID;

import com.sforce.soap.partner.SaveResult;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private static final String TEMPLATE_NAME = "account-aggregation";
	private static final String TEST_FLOWS_FOLDER_PATH = "./src/test/resources/flows/";

	private List<Map<String, Object>> createdAccountsInSalesforce = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> createdAccountsInDatabase = new ArrayList<Map<String, Object>>();
	
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final String PATH_TO_SQL_SCRIPT = "src/main/resources/account.sql";
	private static final String DATABASE_NAME = "SFDC2DBAccountAggregation" + new Long(new Date().getTime()).toString();
	private static final MySQLDbCreator DBCREATOR = new MySQLDbCreator(DATABASE_NAME, PATH_TO_SQL_SCRIPT, PATH_TO_TEST_PROPERTIES);

	@BeforeClass
	public static void init() {
		System.setProperty("db.jdbcUrl", DBCREATOR.getDatabaseUrlWithName());
	}
	
	@Before
	public void setUp() throws Exception {
		DBCREATOR.setUpDatabase();
		createAccounts();
	}

	@After
	public void tearDown() throws Exception {
		deleteTestAccountFromSandBox(createdAccountsInSalesforce, "deleteAccountFromSalesforceFlow");
		deleteTestAccountFromSandBox(createdAccountsInDatabase, "deleteAccountFromDatabaseFlow");
		DBCREATOR.tearDownDataBase();
	}

	@Override
	protected String getConfigResources() {
		return super.getConfigResources() + getTestFlows();
	}

	private String getTestFlows() {
		final File testFlowsFolder = new File(TEST_FLOWS_FOLDER_PATH);
		final File[] listOfFiles = testFlowsFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".xml");
			}
		});

		if (listOfFiles == null) {
			return "";
		}

		final StringBuilder resources = new StringBuilder();
		for (final File f : listOfFiles) {
			resources.append(",").append(TEST_FLOWS_FOLDER_PATH).append(f.getName());
		}
		return resources.toString();
	}

	private void createAccounts() throws Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("createAccountInSalesforceFlow");
		flow.initialise();

		final Map<String, Object> accountA = new HashMap<String, Object>();
		accountA.put("Name", "Name_Salesforce_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		createdAccountsInSalesforce.add(accountA);

		final MuleEvent event = flow.process(getTestEvent(createdAccountsInSalesforce, MessageExchangePattern.REQUEST_RESPONSE));
		final List<?> results = (List<?>) event.getMessage().getPayload();
		for (int i = 0; i < results.size(); i++) {
			createdAccountsInSalesforce.get(i).put(VariableNames.ID, ((SaveResult) results.get(i)).getId());
		}

		flow = getSubFlow("createAccountInDatabaseFlow");
		flow.initialise();
		flow.setMuleContext(muleContext);
		
		final Map<String, Object> accountDatabase = new HashMap<String, Object>();
		accountDatabase.put(VariableNames.NAME, "Name_Database_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		accountDatabase.put(VariableNames.ID, UUID.getUUID());
		accountDatabase.put(VariableNames.INDUSTRY, "Education");
		accountDatabase.put("Description", "Some account description");
		createdAccountsInDatabase.add(accountDatabase);

		flow.process(getTestEvent(createdAccountsInDatabase, MessageExchangePattern.REQUEST_RESPONSE));
	}

	protected void deleteTestAccountFromSandBox(List<Map<String, Object>> createdAccounts, String deleteFlow) throws Exception {
		final List<String> idList = new ArrayList<String>();

		// Delete the created accounts in A
		final SubflowInterceptingChainLifecycleWrapper flow = getSubFlow(deleteFlow);
		flow.initialise();
		for (final Map<String, Object> c : createdAccounts) {
			idList.add((String) c.get(VariableNames.ID));
		}
		flow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
		idList.clear();
	}

	@Test
	public void testGatherDataFlow() throws Exception {
		MuleEvent event = runFlow("gatherDataFlow");
		Iterator<Map<String, String>> mergedList = (Iterator<Map<String, String>>)event.getMessage().getPayload();
		Assert.assertTrue("There should be contacts from source A or source B.", mergedList.hasNext());
	}

}
