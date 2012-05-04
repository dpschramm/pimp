package test.pimp.form;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.Before;

import pimp.form.DoubleFormElement;
import pimp.form.IntFormElement;
import pimp.form.StringFormElement;

public class IntFormElementTest extends junit.framework.TestCase{
	
	IntFormElement ife;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ife = new IntFormElement();
	}
	
	public void testSetIntValue() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		try {
			intFE.setValue(jc, 3);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
	}

	public void testGetValueAfterSetToIntValue() {
		
		IntFormElement intFE = new IntFormElement();
		JComponent jc = intFE.createComponent();
		try {
			intFE.setValue(jc, 4);
		} catch (NullPointerException npe) {
			fail("Should not throw NPE");
		}
		assertTrue(4 == intFE.getValue(jc));
	}

	public void testCreateComponent() {
		
		JComponent jc = ife.createComponent();
		assertNotNull(jc);
		assertTrue(jc instanceof JTextField);
	}
	
	public void testGetInputType() {
		assertTrue((int.class).equals(ife.getInputType()));
	}
}
