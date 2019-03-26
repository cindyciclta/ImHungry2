Feature:
	
	Test sign up functionality

Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"
	And I have navigated to the sign up page
	
Scenario: Navigation check

	Then I should be on the sign up page
	
Scenario: Sign up with duplicate account

	When I sign up with username "test" and password "test"
	Then I should be on the sign up page