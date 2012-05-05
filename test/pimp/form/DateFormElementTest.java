package pimp.form;

import java.util.Date;

import javax.swing.JComponent;

import org.junit.Before;

import pimp.form.DateFormElement;

public class DateFormElementTest extends junit.framework.TestCase implements FormElementTestInterface{
	
	DateFormElement dfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dfe = new DateFormElement();
	}
	
	@Override
	public void testSetValue() {
		
		DateFormElement dateFE = new DateFormElement();
		JComponent jc = dateFE.createComponent();
		try {
			dateFE.setValue(jc, new Date());
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		DateFormElement dateFE = new DateFormElement();
		JComponent jc = dateFE.createComponent();
		Date testDate = new Date();
		try {
			dateFE.setValue(jc, testDate);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(testDate, dateFE.getValue(jc));
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		DateFormElement dateFE = new DateFormElement();
		JComponent jc = dateFE.createComponent();
		assertEquals(null, dateFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		DateFormElement dateFE = new DateFormElement();
		JComponent jc = dateFE.createComponent();
		try {
			dateFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}
	
	@Override
	public void testCreateComponent() {
		
		JComponent jc = dfe.createComponent();
		assertNotNull(jc);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((Date.class).equals(dfe.getInputType()));
	}
}
