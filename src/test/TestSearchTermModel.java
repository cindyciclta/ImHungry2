package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import models.SearchTermModel;

public class TestSearchTermModel {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(new SearchTermModel());
	}

}
