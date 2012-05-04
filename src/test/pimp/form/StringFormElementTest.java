package test.pimp.form;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.StringFormElement;

public class StringFormElementTest extends junit.framework.TestCase implements FormElementTestInterface {
	
	StringFormElement sfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sfe = new StringFormElement();
	}
	
	@Override
	public void testSetValue() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, "Hello");
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, "Hello");
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals("Hello", strFE.getValue(jc));
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		assertEquals("", strFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, null);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
			// Fall through.
		}
	}
	
	@Override
	public void testCreateComponent() {
		
		JComponent jc = sfe.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((String.class).equals(sfe.getInputType()));
	}
}
