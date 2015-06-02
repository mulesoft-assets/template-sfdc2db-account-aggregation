/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.util;

import junit.framework.Assert;

import org.junit.Test;
import org.mule.api.transformer.TransformerException;

@SuppressWarnings("deprecation")
public class DateUtilsTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void nullConcactA() {
		final String dateA = null;
		final String dateB = "2013-12-09T22:15:33.001Z";

		DateUtils.isAfter(dateA, dateB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullConcactB() {
		final String dateA = "2013-12-09T22:15:33.001Z";
		final String dateB = null;

		DateUtils.isAfter(dateA, dateB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void malFormeddateA() throws TransformerException {
		final String dateA = "";
		final String dateB = "2013-12-09T22:15:33.001Z";

		DateUtils.isAfter(dateA, dateB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void malFormeddateB() throws TransformerException {

		final String dateA = "2013-12-09T22:15:33.001Z";
		final String dateB = "";

		DateUtils.isAfter(dateA, dateB);
	}

	@Test
	public void dateAIsAfterdateB() throws TransformerException {
		final String dateA = "2013-12-10T22:15:33.001Z";
		final String dateB = "2013-12-09T22:15:33.001Z";

		Assert.assertTrue("The contact A should be after the contact B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsNotAfterdateB() throws TransformerException {
		final String dateA = "2013-12-08T22:15:33.001Z";
		final String dateB = "2013-12-09T22:15:33.001Z";

		Assert.assertFalse("The contact A should not be after the contact B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsTheSameThatdateB() throws TransformerException {
		final String dateA = "2013-12-09T22:15:33.001Z";
		final String dateB = "2013-12-09T22:15:33.001Z";

		Assert.assertFalse("The contact A should not be after the contact B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsTheSameThatdateBInDifferentTimezone() throws TransformerException {
		final String dateA = "2013-12-09T01:00:33.001-03:00";
		final String dateB = "2013-12-09T07:00:33.001+03:00";

		Assert.assertFalse("The date A should not be after the date B as they are the same ", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsAfterThedateBInDifferentTimezone() throws TransformerException {
		final String dateA = "2013-12-09T02:00:33.001-03:00";
		final String dateB = "2013-12-09T07:00:33.001+03:00";

		Assert.assertTrue("The date A should be after the date B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsBeforeThedateBInDifferentTimezone() throws TransformerException {
		final String dateA = "2013-12-09T01:00:33.001-03:00";
		final String dateB = "2013-12-09T08:00:33.001+03:00";

		Assert.assertFalse("The date A should be after the date B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsTheSameThatdateBInDifferentTimezoneZulu() throws TransformerException {
		final String dateA = "2013-12-09T01:00:33.001-03:00";
		final String dateB = "2013-12-09T04:00:33.001Z";

		Assert.assertFalse("The date A should not be after the date B as they are the same ", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsAfterThedateBInDifferentTimezoneZulu() throws TransformerException {
		final String dateA = "2013-12-09T02:00:33.001-03:00";
		final String dateB = "2013-12-09T04:00:33.001Z";

		Assert.assertTrue("The date A should be after the date B", DateUtils.isAfter(dateA, dateB));
	}

	@Test
	public void dateAIsBeforeThedateBInDifferentTimezoneZulu() throws TransformerException {
		final String dateA = "2013-12-09T02:00:33.001-03:00";
		final String dateB = "2013-12-09T05:00:33.001Z";
		
		Assert.assertFalse("The date A should be after the date B", DateUtils.isAfter(dateA, dateB));
	}

}
