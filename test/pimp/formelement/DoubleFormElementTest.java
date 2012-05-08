package pimp.formelement;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.formelements.DoubleFormElement;
import pimp.formelements.StringFormElement;

public class DoubleFormElementTest extends junit.framework.TestCase implements FormElementTestInterface{
	
	DoubleFormElement dfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dfe = new DoubleFormElement();
	}
	
	@Override
	public void testSetValue() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, 3.09);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, 1.23);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(1.23, doubleFE.getValue(jc));
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		assertEquals(0.0, doubleFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		DoubleFormElement doubleFE = new DoubleFormElement();
		JComponent jc = doubleFE.createComponent();
		try {
			doubleFE.setValue(jc, null);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
			// Expected.
		}
	}
	
	@Override
	public void testCreateComponent() {
		
		JComponent jc = dfe.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((double.class).equals(dfe.getInputType()));
	}
}
