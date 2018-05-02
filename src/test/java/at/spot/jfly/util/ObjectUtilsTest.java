package at.spot.jfly.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class ObjectUtilsTest {

	String name1 = "Test Name 1";
	String name2 = "Test Name 2";

	@Test
	public void testPopulatePropertiesMultipleTimes() {
		Person person = new Person();
		Map<String, Object> properties = new HashMap<>();
		properties.put("name", name1);

		ObjectUtils.populate(person, properties);
		Assert.assertEquals(name1, person.getName());

		properties.put("name", name2);

		ObjectUtils.populate(person, properties);
		Assert.assertEquals(name2, person.getName());

	}

	class Person {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
