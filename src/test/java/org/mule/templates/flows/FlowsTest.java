/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.flows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mule.AbstractTemplateTestCase;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.utils.VariableNames;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template.
 */
public class FlowsTest extends AbstractTemplateTestCase {

	@Test
	public void testAggregationFlow() throws Exception {
		List<Map<String, String>> accountsFromOrgA = createAccountLists("Salesforce", 0, 1);
		List<Map<String, String>> accountsFromOrgB = createAccountLists("Database", 1, 2);

		MuleEvent testEvent = getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE);
		testEvent.getMessage().setInvocationProperty(VariableNames.ACCOUNTS_FROM_SALESFORCE, accountsFromOrgA.iterator());
		testEvent.getMessage().setInvocationProperty(VariableNames.ACCOUNTS_FROM_DATABASE, accountsFromOrgB.iterator());

		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("aggregationFlow");
		flow.initialise();
		MuleEvent event = flow.process(testEvent);

		Assert.assertTrue("The payload should not be null.", event.getMessage().getPayload() != null);
		Assert.assertFalse("The account list should not be empty.", ((List<?>) event.getMessage().getPayload()).isEmpty());
	}

	@Test
	public void testFormatOutputFlow() throws Exception {
		List<Map<String, String>> accountsFromOrgA = createAccountLists("Salesforce", 0, 1);
		List<Map<String, String>> accountsFromOrgB = createAccountLists("Database", 1, 2);

		MuleEvent testEvent = getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE);
		testEvent.getMessage().setInvocationProperty(VariableNames.ACCOUNTS_FROM_SALESFORCE, accountsFromOrgA.iterator());
		testEvent.getMessage().setInvocationProperty(VariableNames.ACCOUNTS_FROM_DATABASE, accountsFromOrgB.iterator());

		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("aggregationFlow");
		flow.initialise();
		MuleEvent event = flow.process(testEvent);

		flow = getSubFlow("formatOutputFlow");
		flow.initialise();
		event = flow.process(event);

		Assert.assertNotNull("The payload should not be null.", event.getMessage().getPayload());
	}

	private List<Map<String, String>> createAccountLists(String orgId, int start, int end) {
		List<Map<String, String>> accountList = new ArrayList<Map<String, String>>();
		for (int i = start; i <= end; i++) {
			accountList.add(createAccount(orgId, i));
		}
		return accountList;
	}

	private Map<String, String> createAccount(String orgId, int sequence) {
		Map<String, String> account = new HashMap<String, String>();

		account.put("Id", new Integer(sequence).toString());
		account.put("Name", "SomeName_" + sequence);
		account.put("Email", "some.email." + sequence + "@fakemail.com");

		return account;
	}

}
