/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.routing.AggregationContext;
import org.mule.AbstractTemplateTestCase;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class AccountMergeAggregationStrategyTest extends AbstractTemplateTestCase {
	
	@Mock
	private MuleContext muleContext;
  
	
	@Test
	public void testAggregate() throws Exception {
		List<Map<String, String>> accountsA = AccountsMergeTest.createAccountsSalesforce();
		List<Map<String, String>> accountsB = AccountsMergeTest.createAccountsDatabase();
		
		MuleEvent testEventA = getTestEvent("");
		MuleEvent testEventB = getTestEvent("");
		
		testEventA.getMessage().setPayload(accountsA.iterator());
		testEventB.getMessage().setPayload(accountsB.iterator());
		
		List<MuleEvent> testEvents = new ArrayList<MuleEvent>();
		testEvents.add(testEventA);
		testEvents.add(testEventB);
		
		AggregationContext aggregationContext = new AggregationContext(getTestEvent(""), testEvents);
		
		AccountMergeAggregationStrategy accountMergeStrategy = new AccountMergeAggregationStrategy();
		Iterator<Map<String, String>> iterator = (Iterator<Map<String, String>>) accountMergeStrategy.aggregate(aggregationContext).getMessage().getPayload();
		List<Map<String, String>> mergedList = Lists.newArrayList(iterator);

		Assert.assertEquals("The merged list obtained is not as expected", AccountsMergeTest.createExpectedList(), mergedList);

	}

}
