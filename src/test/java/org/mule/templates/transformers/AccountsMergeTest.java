/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.db.MySQLDbCreator;
import org.mule.templates.utils.Utils;
import org.mule.templates.utils.VariableNames;

@SuppressWarnings({ "deprecation", "unused" })
@RunWith(MockitoJUnitRunner.class)
public class AccountsMergeTest {
	
	private static final Logger LOG = LogManager.getLogger(AccountsMergeTest.class);

	@Mock
	private MuleContext muleContext;

	@Test
	public void testMerge() throws TransformerException {
		final List<Map<String, String>> accountsSalesforce = createAccountsSalesforce();

		final List<Map<String, String>> accountsDatabase = createAccountsDatabase();
		
		final AccountsMerge accountsMerge = new AccountsMerge();
		final List<Map<String, String>> mergedList = accountsMerge.mergeList(accountsSalesforce, accountsDatabase);

		LOG.info("Merged list is: " + mergedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), mergedList);
	}
	
	static List<Map<String, String>> createAccountsSalesforce() {
		List<Map<String, String>> accountsSalesforce = new ArrayList<Map<String,String>>();
		
		final Map<String, String> account0Salesforce = new HashMap<String, String>();
		account0Salesforce.put(VariableNames.ID, "0");
		account0Salesforce.put(VariableNames.NAME, "Sony");
		account0Salesforce.put(VariableNames.INDUSTRY, "Entertaiment");
		account0Salesforce.put(VariableNames.NUMBER_OF_EMPLOYEES, "28");
		accountsSalesforce.add(account0Salesforce);

		final Map<String, String> account1Salesforce = new HashMap<String, String>();
		account1Salesforce.put(VariableNames.ID, "1");
		account1Salesforce.put(VariableNames.NAME, "Generica");
		account1Salesforce.put(VariableNames.INDUSTRY, "Pharmaceutic");
		account1Salesforce.put(VariableNames.NUMBER_OF_EMPLOYEES, "22");
		accountsSalesforce.add(account1Salesforce);
		
		return accountsSalesforce;
	}

	static List<Map<String, String>> createAccountsDatabase() {
		List<Map<String, String>> accountsDatabase = new ArrayList<Map<String,String>>();
		
		final Map<String, String> account1Database = new HashMap<String, String>();
		account1Database.put(VariableNames.ID, "1");
		account1Database.put(VariableNames.NAME, "Generica");
		account1Database.put(VariableNames.INDUSTRY, "Experimental");
		account1Database.put(VariableNames.NUMBER_OF_EMPLOYEES, "500");
		accountsDatabase.add(account1Database);

		final Map<String, String> account2Database = new HashMap<String, String>();
		account2Database.put("Id", "2");
		account2Database.put(VariableNames.NAME, "Global Voltage");
		account2Database.put("Industry", "Energetic");
		account2Database.put("NumberOfEmployees", "4160");
		accountsDatabase.add(account2Database);
		
		return accountsDatabase;
	}

	static List<Map<String, String>> createExpectedList() {
		final Map<String, String> record0 = new HashMap<String, String>();
		record0.put(VariableNames.ID_IN_SALESFORCE, "0");
		record0.put(VariableNames.ID_IN_DATABASE, "");
		record0.put(VariableNames.INDUSTRY_IN_SALESFORCE, "Entertaiment");
		record0.put(VariableNames.INDUSTRY_IN_DATABASE, "");
		record0.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "28");
		record0.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "");
		record0.put(VariableNames.NAME, "Sony");

		final Map<String, String> record1 = new HashMap<String, String>();
		record1.put(VariableNames.ID_IN_SALESFORCE, "1");
		record1.put(VariableNames.ID_IN_DATABASE, "1");
		record1.put(VariableNames.INDUSTRY_IN_SALESFORCE, "Pharmaceutic");
		record1.put(VariableNames.INDUSTRY_IN_DATABASE, "Experimental");
		record1.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "22");
		record1.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "500");
		record1.put(VariableNames.NAME, "Generica");

		final Map<String, String> record2 = new HashMap<String, String>();
		record2.put(VariableNames.ID_IN_SALESFORCE, "");
		record2.put(VariableNames.ID_IN_DATABASE, "2");
		record2.put(VariableNames.INDUSTRY_IN_SALESFORCE, "");
		record2.put(VariableNames.INDUSTRY_IN_DATABASE, "Energetic");
		record2.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "");
		record2.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, "4160");
		record2.put(VariableNames.NAME, "Global Voltage");

		final List<Map<String, String>> expectedList = new ArrayList<Map<String, String>>();
		expectedList.add(record0);
		expectedList.add(record1);
		expectedList.add(record2);

		return expectedList;
	}

}
