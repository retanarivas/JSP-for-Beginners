function validateForm() {

	var errorFields = [];
	
	var studentForm = document.forms["studentForm"];
	
	var firstName = studentForm["firstName"].value.trim();
	if(firstName == "") {
		errorFields.push("Firs name");
	}
	
	var lastName = studentForm["lastName"].value.trim();
	if(lastName == "") {
		errorFields.push("Last name");
	}
	
	var email = studentForm["email"].value.trim();
	if(email == "") {
		errorFields.push("Email");
	}
	
	if(errorFields.length > 0) {
		alert("Form validation failed. Please add data for following fields: " + errorFields);
		return false;
	}
	
}