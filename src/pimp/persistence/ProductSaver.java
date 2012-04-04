package pimp.persistence;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Product;

public abstract class ProductSaver {
	
	private boolean isGetter(Method method) {
		if (!method.getName().startsWith("get")) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (void.class.equals(method.getReturnType())) {
			return false;
		}
		
		return true;
	}
	
	
	protected List<Method> getGetters(Class c) {
		List<Method> getters = new ArrayList<Method>();
		Method[] methods = c.getMethods();
		
		
		for (Method method : methods) {
			if (isGetter(method)) {
				getters.add(method);
			}
		}
		
		return getters;
	}
	
	
	public abstract boolean save(Product prod);
}
