/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;
import org.mule.templates.utils.Utils;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will sort a list of map defining a weight for each map base
 * on the value of its keys.
 * 
 * @author damian.sima
 * @author martin
 */
public final class SortAccountList extends AbstractMessageTransformer {
	
	private static final Comparator<Map<String, String>> ACCOUNT_MAP_COMPARATOR = new Comparator<Map<String, String>>() {

		@Override
		public int compare(Map<String, String> user1, Map<String, String> user2) {
			String key1 = buildKey(user1);
			String key2 = buildKey(user2);

			return key1.compareTo(key2);
		}

		private String buildKey(Map<String, String> user) {
			StringBuilder key = new StringBuilder();

			if (StringUtils.isNotBlank(user.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isNotBlank(user.get(VariableNames.ID_IN_DATABASE))) {
				key.append("~~~").append(user.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			if (StringUtils.isNotBlank(user.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isBlank(user.get(VariableNames.ID_IN_DATABASE))) {
				key.append("~").append(user.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			if (StringUtils.isBlank(user.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isNotBlank(user.get(VariableNames.ID_IN_DATABASE))) {
				key.append("~~").append(user.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			return key.toString();
		}

	};

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		List<Map<String, String>> sortedUsersList = Utils.buildList(message.getPayload());
		Collections.sort(sortedUsersList, ACCOUNT_MAP_COMPARATOR);
		return sortedUsersList;
	}
	
}
