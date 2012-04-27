package pimp.testdefs;

import pimp.form.CompanionForm;
import pimp.ConctreteCompanionFormExample;

public class CompanionFormTestClass {
	
	public String name;
	CompanionForm form = new ConctreteCompanionFormExample();
	
	public CompanionFormTestClass() {
		// TODO Auto-generated constructor stub
	}
	
	public CompanionForm getCompanionForm(){
		return form;
	}
	
	
}
