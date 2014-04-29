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
public class SortAccountListTransformerTest {
	
	@Mock
	private MuleContext muleContext;

	@Test
	public void testSort() throws TransformerException {
		MuleMessage message = new DefaultMuleMessage(createOriginalList(), muleContext);

		SortAccountListTransformer transformer = new SortAccountListTransformer();
		List<Map<String, String>> sortedList = Utils.buildList(transformer.transform(message, "UTF-8"));

		System.out.println(sortedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), sortedList);
	}

	private List<Map<String, String>> createExpectedList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put("IDInA", "0");
		record0.put("IDInB", "");
		record0.put("Name", "SomeName_0");
		record0.put("IndustryInA", "industry_0_A");
		record0.put("IndustryInB", "");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put("IDInA", "1");
		record1.put("IDInB", "1");
		record1.put("Name", "SomeName_1");
		record1.put("IndustryInA", "industry_1_A");
		record1.put("IndustryInB", "industry_1_B");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put("IDInA", "");
		record2.put("IDInB", "2");
		record2.put("Name", "SomeName_2");
		record2.put("IndustryInA", "");
		record2.put("IndustryInB", "industry_2_B");

		List<Map<String, String>> expectedList = new ArrayList<Map<String, String>>();
		expectedList.add(record0);
		expectedList.add(record2);
		expectedList.add(record1);

		return expectedList;

	}

	private List<Map<String, String>> createOriginalList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put("IDInA", "0");
		record0.put("IDInB", "");
		record0.put("Name", "SomeName_0");
		record0.put("IndustryInA", "industry_0_A");
		record0.put("IndustryInB", "");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put("IDInA", "1");
		record1.put("IDInB", "1");
		record1.put("Name", "SomeName_1");
		record1.put("IndustryInA", "industry_1_A");
		record1.put("IndustryInB", "industry_1_B");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put("IDInA", "");
		record2.put("IDInB", "2");
		record2.put("Name", "SomeName_2");
		record2.put("IndustryInA", "");
		record2.put("IndustryInB", "industry_2_B");

		List<Map<String, String>> originalList = new ArrayList<Map<String, String>>();
		originalList.add(record0);
		originalList.add(record1);
		originalList.add(record2);

		return originalList;

	}

}
