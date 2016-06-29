package de.afrouper.beans.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import de.afrouper.beans.Bean;

public class BeanScanner {

	private static final BeanScanner ref = new BeanScanner();

	private Map<Class<? extends Bean>, BeanDescription> scannedBeans;

	private BeanScanner() {
		scannedBeans = new ConcurrentHashMap<>();
	}

	public static BeanScanner get() {
		return ref;
	}

	BeanDescription getBeanDescription(Class<? extends Bean> beanClass) {
		BeanDescription descr = scannedBeans.get(beanClass);
		if (descr == null) {
			descr = createBeanDescription(beanClass);
			scannedBeans.put(beanClass, descr);
		}
		return descr;
	}

	private BeanDescription createBeanDescription(Class<? extends Bean> beanClass) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
			BeanDescription description = new BeanDescription(beanClass,
					beanInfo.getBeanDescriptor().getName());
			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				if (BeanHelper.isValidProperty(property.getName())) {
					handlePropertyDescriptor(description, property);
				}
			}
			return description;
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to create BeanDescription from beanClass " + beanClass.getName(),
					e);
		}
	}

	private void handlePropertyDescriptor(BeanDescription description, PropertyDescriptor property) {
		BeanProperty beanProperty = new BeanProperty(property.getName());
		checkAndInvoke(property.getReadMethod(), beanProperty::setReadMethodName);
		checkAndInvoke(property.getWriteMethod(), beanProperty::setWriteMethodName);
		description.addBeanProperty(beanProperty);
	}

	private void checkAndInvoke(Method method, Consumer<String> consumer) {
		if (method != null && BeanHelper.isValidMethod(method.getName())) {
			consumer.accept(method.getName());
		}

	}
}