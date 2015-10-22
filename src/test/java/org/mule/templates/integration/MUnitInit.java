package org.mule.templates.integration;

import org.junit.BeforeClass;
import org.mule.AbstractTemplateTestCase;

public class MUnitInit extends AbstractTemplateTestCase {

	@BeforeClass
	public static void init() {
		//System.setProperty("db.jdbcUrl", DBCREATOR.getDatabaseUrlWithName());
	}
	
	
}
