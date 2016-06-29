package de.afrouper.beans.impl;

import org.junit.Assert;
import org.junit.Test;

import de.afrouper.beans.api.BeanList;

public class BeanListTest {

	private static final int MAX = 5;

	@Test
	public void simpleList() {
		BeanList<Person> persons = createPersonsList();
		Assert.assertEquals(MAX, persons.size());

		for (int i = 0; i < MAX; ++i) {
			Person person = persons.get(i);
			Assert.assertEquals(i, person.getAge().intValue());
			Assert.assertEquals("Name_" + (i + 1), person.getName());
		}
	}

	@Test
	public void childLists() {
		Person person = BeanFactory.createBean(Person.class);
		person.setChilds(createPersonsList());

		Assert.assertNotNull(person.getChilds());
		Assert.assertEquals(MAX, person.getChilds().size());
	}

	private BeanList<Person> createPersonsList() {
		BeanList<Person> persons = BeanFactory.createBeanList(Person.class);
		Assert.assertNotNull(persons);

		for (int i = 0; i < MAX; ++i) {
			Person person = BeanFactory.createBean(Person.class);
			person.setAge(i);
			person.setName("Name_" + (i + 1));
			persons.add(person);
		}
		return persons;
	}
}
