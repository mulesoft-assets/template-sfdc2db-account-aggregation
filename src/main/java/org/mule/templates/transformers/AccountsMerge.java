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

import org.apache.commons.lang.StringUtils;
import org.mule.templates.utils.VariableNames;

/**
 * This transformer will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 * 
 * @author damian.sima
 */
public class AccountsMerge {

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromA
	 *            accounts from organization A
	 * @param accountsFromB
	 *            accounts from database
	 * @return a list with the merged content of the to input lists
	 */
	List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromA, List<Map<String, String>> accountsFromB) {
		List<Map<String, String>> mergedAccountList = new ArrayList<Map<String, String>>();

		// Put all accounts from A in the merged List
		for (Map<String, String> accountFromA : accountsFromA) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromA);
			mergedAccount.put(VariableNames.ID_IN_SALESFORCE, accountFromA.get(VariableNames.ID));
			mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, accountFromA.get(VariableNames.INDUSTRY));
			mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, accountFromA.get(VariableNames.NUMBER_OF_EMPLOYEES));
			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from B and update the exiting ones
				for (Map<String, String> accountFromB : accountsFromB) {
					Map<String, String> accountFromA = findAccountInList(accountFromB.get(VariableNames.IDENTITY_FIELD_KEY), mergedAccountList);
					if (accountFromA != null) {
						accountFromA.put(VariableNames.ID_IN_DATABASE, accountFromB.get(VariableNames.ID));
						accountFromA.put(VariableNames.INDUSTRY_IN_DATABASE, accountFromB.get(VariableNames.INDUSTRY));
						accountFromA.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, accountFromB.get(VariableNames.NUMBER_OF_EMPLOYEES));
					} else {
						Map<String, String> mergedAccount = createMergedAccount(accountFromB);
						mergedAccount.put(VariableNames.ID_IN_DATABASE, accountFromB.get(VariableNames.ID));
						mergedAccount.put(VariableNames.INDUSTRY_IN_DATABASE, accountFromB.get(VariableNames.INDUSTRY));
						mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, accountFromB.get(VariableNames.NUMBER_OF_EMPLOYEES));
						mergedAccountList.add(mergedAccount);
					}

		}
		return mergedAccountList;
	}

	private static Map<String, String> createMergedAccount(Map<String, String> account) {
		Map<String, String> mergedAccount = new HashMap<String, String>();
		mergedAccount.put(VariableNames.IDENTITY_FIELD_KEY, account.get(VariableNames.IDENTITY_FIELD_KEY));
		mergedAccount.put(VariableNames.ID_IN_SALESFORCE, StringUtils.EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, StringUtils.EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, StringUtils.EMPTY);
		mergedAccount.put(VariableNames.ID_IN_DATABASE, StringUtils.EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_DATABASE, StringUtils.EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, StringUtils.EMPTY);
		return mergedAccount;
	}

	private Map<String, String> findAccountInList(String accountName, List<Map<String, String>> orgList) {
		for (Map<String, String> account : orgList) {
			if (account.get("Name").equals(accountName)) {
				return account;
			}
		}
		return null;
	}

}
