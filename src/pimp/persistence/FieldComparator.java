package pimp.persistence;

import java.lang.reflect.Field;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field> {

	@Override
	public int compare(Field field1, Field field2) {
		return field1.getName().compareTo(field2.getName());
	}

}
