package test.java;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import models.SearchModel;

public class TestSearchModel {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(new SearchModel());
	}

}
