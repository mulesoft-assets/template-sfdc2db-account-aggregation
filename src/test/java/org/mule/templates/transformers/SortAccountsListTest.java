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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;

/**
 * The test validates that the {@link SortAccountsList} properly order a list of
 * maps based on it internal criteria.
 * 
 * @author damiansima
 */
@RunWith(MockitoJUnitRunner.class)
public class SortAccountsListTest {

	@Mock
	private MuleContext muleContext;

	@Test
	@SuppressWarnings("unchecked")
	public void testSort() throws TransformerException {
		List<Map<String, String>> originalList = createOriginalList();
		MuleMessage message = new DefaultMuleMessage(originalList.iterator(), muleContext);

		SortAccountsList transformer = new SortAccountsList();
		List<Map<String, String>> sortedList = (List<Map<String, String>>) transformer
				.transform(message, "UTF-8");

		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), sortedList);

	}
	
	private Object createExpectedList() {
		List<Map<String, String>> originalList = new ArrayList<Map<String, String>>();
		originalList.add(createAccount("0"));
		originalList.add(createAccount("1"));
		originalList.add(createAccount("2"));
		
		return originalList;
	}

	private List<Map<String, String>> createOriginalList() {
		List<Map<String, String>> originalList = new ArrayList<Map<String, String>>();
		originalList.add(createAccount("0"));
		originalList.add(createAccount("2"));
		originalList.add(createAccount("1"));
		
		return originalList;

	}

	private Map<String, String> createAccount(String id) {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put(VariableNames.ID_IN_SALESFORCE, id);
		record0.put(VariableNames.ID_IN_DATABASE, "");
		record0.put(VariableNames.NAME, "SomeName_" + id);
		record0.put(VariableNames.INDUSTRY_IN_SALESFORCE, "industry_" + id + "_Salesforce");
		record0.put(VariableNames.INDUSTRY_IN_DATABASE, "");
		return record0;
	}	

}
