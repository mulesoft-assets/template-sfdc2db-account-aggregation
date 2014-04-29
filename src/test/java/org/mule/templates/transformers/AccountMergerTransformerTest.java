package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

@RunWith(MockitoJUnitRunner.class)
public class AccountMergerTransformerTest {
	private static final String ACCOUNTS_FROM_SFDC = "accountsFromOrgA";
	private static final String ACCOUNTS_FROM_DB = "accountsFromOrgB";

	@Mock
	private MuleContext muleContext;

	@Test
	public void testMerge() throws TransformerException {
		List<Map<String, String>> accountsA = new ArrayList<Map<String,String>>();

		Map<String, String> account0A = new HashMap<String, String>();
		account0A.put("Id", "0");
		account0A.put("Name", "Sony");
		account0A.put("Industry", "Entertaiment");
		account0A.put("NumberOfEmployees", "28");
		accountsA.add(account0A);

		Map<String, String> account1A = new HashMap<String, String>();
		account1A.put("Id", "1");
		account1A.put("Name", "Generica");
		account1A.put("Industry", "Pharmaceutic");
		account1A.put("NumberOfEmployees", "22");
		accountsA.add(account1A);
		
		List<Map<String, String>> accountsB = new ArrayList<Map<String,String>>();
		
		Map<String, String> account1B = new HashMap<String, String>();
		account1B.put("Id", "1");
		account1B.put("Name", "Generica");
		account1B.put("Industry", "Experimental");
		account1B.put("NumberOfEmployees", "500");
		accountsB.add(account1B);

		Map<String, String> account2B = new HashMap<String, String>();
		account2B.put("Id", "2");
		account2B.put("Name", "Global Voltage");
		account2B.put("Industry", "Energetic");
		account2B.put("NumberOfEmployees", "4160");
		accountsB.add(account2B);

		MuleMessage message = new DefaultMuleMessage(null, muleContext);
		message.setInvocationProperty(ACCOUNTS_FROM_SFDC, accountsA.iterator());
		message.setInvocationProperty(ACCOUNTS_FROM_DB, accountsB.iterator());

		AccountMergerTransformer transformer = new AccountMergerTransformer();
		List<Map<String, String>> mergedList = Utils.buildList(transformer.transform(message, "UTF-8"));

		System.out.println(mergedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), mergedList);
	}

	private List<Map<String, String>> createExpectedList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put("IDInA", "0");
		record0.put("IDInB", "");
		record0.put("IndustryInA", "Entertaiment");
		record0.put("IndustryInB", "");
		record0.put("NumberOfEmployeesInA", "28");
		record0.put("NumberOfEmployeesInB", "");
		record0.put("Name", "Sony");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put("IDInA", "1");
		record1.put("IDInB", "1");
		record1.put("IndustryInA", "Pharmaceutic");
		record1.put("IndustryInB", "Experimental");
		record1.put("NumberOfEmployeesInA", "22");
		record1.put("NumberOfEmployeesInB", "500");
		record1.put("Name", "Generica");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put("IDInA", "");
		record2.put("IDInB", "2");
		record2.put("IndustryInA", "");
		record2.put("IndustryInB", "Energetic");
		record2.put("NumberOfEmployeesInA", "");
		record2.put("NumberOfEmployeesInB", "4160");
		record2.put("Name", "Global Voltage");

		List<Map<String, String>> expectedList = new ArrayList<Map<String, String>>();
		expectedList.add(record0);
		expectedList.add(record1);
		expectedList.add(record2);

		return expectedList;
	}

}
