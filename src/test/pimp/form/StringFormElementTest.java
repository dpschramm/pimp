package test.pimp.form;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.StringFormElement;

public class StringFormElementTest extends junit.framework.TestCase{
	
	StringFormElement sfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sfe = new StringFormElement();
	}
	
	public void testSetStringValue() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, "Hello");
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	public void testGetValueAfterSetToStringValue() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, "Hello");
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals("Hello", strFE.getValue(jc));
	}
	
	public void testGetValueAfterNothingSet() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		assertEquals("", strFE.getValue(jc));
	}
	
	public void testSetValueNull() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	public void testGetValueAfterSetToNullIsNull() {
		
		StringFormElement strFE = new StringFormElement();
		JComponent jc = strFE.createComponent();
		try {
			strFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertNull(strFE.getValue(jc));
	}
	
	public void testCreateComponent() {
		
		JComponent jc = sfe.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	public void testGetInputType() {
		assertTrue((String.class).equals(sfe.getInputType()));
	}
}
