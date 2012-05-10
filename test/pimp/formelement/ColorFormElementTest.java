package pimp.formelement;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.elements.ColorFormElement;
import pimp.form.elements.StringFormElement;

public class ColorFormElementTest extends junit.framework.TestCase implements FormElementTestInterface{
	
	ColorFormElement cfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cfe = new ColorFormElement();
	}
	
	@Override
	public void testSetValue() {
		
		ColorFormElement colorFE = new ColorFormElement();
		JComponent jc = colorFE.createComponent();
		try {
			colorFE.setValue(jc, Color.BLUE);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		ColorFormElement colorFE = new ColorFormElement();
		JComponent jc = colorFE.createComponent();
		try {
			colorFE.setValue(jc, Color.BLUE);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(Color.BLUE, colorFE.getValue(jc));
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		ColorFormElement colorFE = new ColorFormElement();
		JComponent jc = colorFE.createComponent();
		assertEquals(new Color(238, 238, 238), colorFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		ColorFormElement colorFE = new ColorFormElement();
		JComponent jc = colorFE.createComponent();
		try {
			colorFE.setValue(jc, null);
			fail("Should not throw NPE");
		} catch (NullPointerException npe) {
			// Expected.
		}
	}
	
	@Override
	public void testCreateComponent() {
		
		JComponent jc = cfe.createComponent();
		assertNotNull(jc);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((Color.class).equals(cfe.getInputType()));
	}
}
