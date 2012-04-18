package test.pimp.form;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.DoubleFormElement;
import pimp.form.StringFormElement;

public class DoubleFormElementTest extends junit.framework.TestCase{
	
	DoubleFormElement dfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dfe = new DoubleFormElement();
	}
	
	public void testSetDoubleValue() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, 3.09);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	public void testGetValueAfterSetToDoubleValue() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, 1.23);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(1.23, doubleFE.getValue(jc));
	}
	
	public void testGetValueAfterNothingSet() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		assertEquals(0.0, doubleFE.getValue(jc));
	}
	
	public void testSetValueNull() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	public void testGetValueAfterSetToNullIsNull() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(0.0, doubleFE.getValue(jc));
	}
	
	public void testCreateComponent() {
		
		JComponent jc = dfe.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	public void testGetInputType() {
		assertTrue((double.class).equals(dfe.getInputType()));
	}
}
