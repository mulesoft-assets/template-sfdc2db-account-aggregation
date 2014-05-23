package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;
import org.mule.templates.utils.Utils;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 * 
 * @author damian.sima
 * @author martin
 */
public final class AccountMergerTransformer extends AbstractMessageTransformer {

	private static final String EMPTY = "";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		List<Map<String, String>> mergedAccountsList = mergeList(
				Utils.buildList(message, VariableNames.ACCOUNTS_FROM_SALESFORCE),
				Utils.buildList(message, VariableNames.ACCOUNTS_FROM_DATABASE));

		return mergedAccountsList;
	}

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromOrgA
	 *            accounts from organization A
	 * @param accountsFromOrgB
	 *            accounts from organization B
	 * @return a list with the merged content of the to input lists
	 */
	private static List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromOrgA, List<Map<String, String>> accountsFromOrgB) {
		List<Map<String, String>> mergedAccountList = new ArrayList<Map<String, String>>();

		// Put all accounts from A in the merged contactList
		for (Map<String, String> accountFromA : accountsFromOrgA) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromA);
			mergedAccount.put(VariableNames.ID_IN_SALESFORCE, accountFromA.get(VariableNames.ID));
			mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, accountFromA.get(VariableNames.INDUSTRY));
			mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, accountFromA.get(VariableNames.NUMBER_OF_EMPLOYEES));
			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from B and update the exiting ones
		for (Map<String, String> accountFromB : accountsFromOrgB) {
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
		mergedAccount.put(VariableNames.ID_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.ID_IN_DATABASE, EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_DATABASE, EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_DATABASE, EMPTY);
		return mergedAccount;
	}

	private static Map<String, String> findAccountInList(String accountName, List<Map<String, String>> accountList) {
		for (Map<String, String> account : accountList) {
			if (account.get(VariableNames.IDENTITY_FIELD_KEY).equals(accountName)) {
				return account;
			}
		}
		return null;
	}

}
