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
	
Scenario: Sign up with password != passwordConfirmation

	When I sign up with username "user", password "pass", and passwordConf "wrong"
	Then I should be on the sign up page
	
Scenario: Sign up with empty username

	When I sign up with username "" and password "pass"
	Then I should be on the sign up page
	
Scenario: Sign up with empty password

	When I sign up with username "user" and password ""
	Then I should be on the sign up page
	
Scenario: Sign in with incorrect password

	When I sign in with username "test" and password "wrong"
	Then I should be on the sign in page
	
Scenario: Sign in with empty username

	When I sign in with username "" and password "pass"
	Then I should be on the sign in page
	
Scenario: Sign in with empty password

	When I sign in with username "user" and password ""
	Then I should be on the sign in page
	
Scenario: Sign out from results page

	When I searched for item "chinese" with "5" results and was redirected to the Results page
	And I click the sign out button
	Then I should be on the sign in page
	
Scenario: Sign out from search page

	When I am on the search page
	And I click the sign out button
	Then I should be on the sign in page
	
Scenario: Test that https is a substring of the url

	When I am on the search page
	Then the url should contain "https"
	When I searched for item "water" with "1" results and was redirected to the Results page
	Then the url should contain "https"
	
Scenario: Test that going directly to the search/result page without signing in leads to sign in page

	When I am on the search page
	And I click the sign out button
	And I go directly to the search page controller
	Then I should be on the sign in page
	When I go directly to the results page controller
	Then I should be on the sign in page
	When I go directly to the search page jsp
	Then I should be on the sign in page
	When I go directly to the results page jsp
	Then I should be on the sign in page
	When I go directly to the manage list jsp
	Then I should be on the sign in page