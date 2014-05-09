package org.mule.kicks.integration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.AbstractTemplateTestCase;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
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

	private static final String ACCOUNTS_FROM_ORG_A = "accountsFromOrgA";
	private static final String ACCOUNTS_FROM_ORG_B = "accountsFromOrgB";

	private List<Map<String, Object>> createdAccountsInA = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> createdAccountsInB = new ArrayList<Map<String, Object>>();

	@Before
	public void setUp() throws Exception {
		createAccounts();
	}

	@After
	public void tearDown() throws Exception {
		deleteTestAccountFromSandBox(createdAccountsInA, "deleteAccountFromAFlow");
		deleteTestAccountFromSandBox(createdAccountsInB, "deleteAccountFromBFlow");
	}

	@Override
	protected String getConfigResources() {
		return super.getConfigResources() + getTestFlows();
	}

	private String getTestFlows() {
		File testFlowsFolder = new File(TEST_FLOWS_FOLDER_PATH);
		File[] listOfFiles = testFlowsFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".xml");
			}
		});

		if (listOfFiles == null) {
			return "";
		}

		StringBuilder resources = new StringBuilder();
		for (File f : listOfFiles) {
			resources.append(",").append(TEST_FLOWS_FOLDER_PATH).append(f.getName());
		}
		return resources.toString();
	}

	private void createAccounts() throws Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("createAccountInAFlow");
		flow.initialise();

		Map<String, Object> accountA = new HashMap<String, Object>();
		accountA.put("Name", "Name_A_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		createdAccountsInA.add(accountA);

		MuleEvent event = flow.process(getTestEvent(createdAccountsInA, MessageExchangePattern.REQUEST_RESPONSE));
		List<?> results = (List<?>) event.getMessage().getPayload();
		for (int i = 0; i < results.size(); i++) {
			createdAccountsInA.get(i).put("Id", ((SaveResult) results.get(i)).getId());
		}

		flow = getSubFlow("createAccountInBFlow");
		flow.initialise();

		Map<String, Object> accountB = new HashMap<String, Object>();
		accountB.put("Name", "Name_B_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		accountB.put("Id", UUID.getUUID());
		accountB.put("Industry", "Education");
		accountB.put("Description", "Some account description");
		createdAccountsInB.add(accountB);

		flow.process(getTestEvent(createdAccountsInB, MessageExchangePattern.REQUEST_RESPONSE));
	}

	protected void deleteTestAccountFromSandBox(List<Map<String, Object>> createdAccounts, String deleteFlow) throws Exception {
		List<String> idList = new ArrayList<String>();

		// Delete the created accounts in A
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow(deleteFlow);
		flow.initialise();
		for (Map<String, Object> c : createdAccounts) {
			idList.add((String) c.get("Id"));
		}
		flow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
		idList.clear();
	}

	@Test
	public void testGatherDataFlow() throws Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("gatherDataFlow");
		flow.initialise();

		MuleEvent event = flow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		Set<String> flowVariables = event.getFlowVariableNames();

		Assert.assertTrue("The variable accountsFromOrgA is missing.", flowVariables.contains(ACCOUNTS_FROM_ORG_A));
		Assert.assertTrue("The variable accountsFromOrgB is missing.", flowVariables.contains(ACCOUNTS_FROM_ORG_B));

		Iterator<?> accountsFromOrgA = event.getFlowVariable(ACCOUNTS_FROM_ORG_A);
		Collection<?> accountsFromOrgB = event.getFlowVariable(ACCOUNTS_FROM_ORG_B);

		Assert.assertTrue("There should be accounts in the variable accountsFromOrgA.", accountsFromOrgA.hasNext());
		Assert.assertTrue("There should be accounts in the variable accountsFromOrgB.", !accountsFromOrgB.isEmpty());
	}

}
