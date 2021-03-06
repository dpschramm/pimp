package pimp.formelement;

import java.awt.Color;

import javax.swing.JComponent;

import org.junit.Before;

import pimp.form.elements.ColorFormElement;
import pimp.form.elements.NullDecoratorFormElement;

public class NullDecoratorFormElementTest extends junit.framework.TestCase implements FormElementTestInterface{
	
	NullDecoratorFormElement<?> ndfe;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ndfe = new NullDecoratorFormElement<Color>(new ColorFormElement());
	}
	
	@Override
	public void testSetValue() {
		
		NullDecoratorFormElement<Color> nullableFE = new NullDecoratorFormElement<Color>(new ColorFormElement());
		JComponent jc = nullableFE.createComponent();
		try {
			nullableFE.setValue(jc, Color.BLACK);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	@Override
	public void testGetValueAfterSetToValue() {
		
		NullDecoratorFormElement<Color> nullableFE = new NullDecoratorFormElement<Color>(new ColorFormElement());
		JComponent jc = nullableFE.createComponent();
		try {
			nullableFE.setValue(jc, Color.BLACK);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertEquals(Color.BLACK, nullableFE.getValue(jc));
	}
	
	@Override
	public void testGetValueAfterNothingSet() {
		
		NullDecoratorFormElement<Color> nullableFE = new NullDecoratorFormElement<Color>(new ColorFormElement());
		JComponent jc = nullableFE.createComponent();
		assertEquals(new Color(238, 238, 238), nullableFE.getValue(jc));
	}
	
	@Override
	public void testSetValueNull() {
		
		NullDecoratorFormElement<Color> nullableFE = new NullDecoratorFormElement<Color>(new ColorFormElement());
		JComponent jc = nullableFE.createComponent();
		try {
			nullableFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}
	
	@Override
	public void testCreateComponent() {
		
		JComponent jc = ndfe.createComponent();
		assertNotNull(jc);
	}
	
	@Override
	public void testGetInputType() {
		assertTrue((Color.class).equals(ndfe.getInputType()));
	}
	
	public void testGetValueAfterSetValueNull() {
		
		NullDecoratorFormElement<Color> nullableFE = new NullDecoratorFormElement<Color>(new ColorFormElement());
		JComponent jc = nullableFE.createComponent();
		try {
			nullableFE.setValue(jc, null);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertNull(nullableFE.getValue(jc));
	}
}
