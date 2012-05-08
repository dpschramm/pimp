package pimp.formelement;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.elements.DoubleFormElement;
import pimp.form.elements.IntFormElement;
import pimp.form.elements.StringFormElement;

public class IntFormElementTest extends junit.framework.TestCase implements FormElementTestInterface {
	
	IntFormElement ife;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ife = new IntFormElement();
	}
	
	@Override
	public void testSetValue() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		try {
			intFE.setValue(jc, 3);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		try {
			intFE.setValue(jc, 4);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertTrue(4 == intFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		try {
			intFE.setValue(jc, null);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
			// Expected.
		}
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		assertTrue(0 == intFE.getValue(jc));
	}

	@Override
	public void testCreateComponent() {
		
		JComponent jc = ife.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((int.class).equals(ife.getInputType()));
	}
}
