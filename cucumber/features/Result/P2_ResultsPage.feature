Feature:

	Project 2 tests for new Results Page features
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"

Scenario: No results because small radius
	
	When I searched for item "Indian" with "3" radius and was redirected to the Results page
	Then I should not see any restaurants

Scenario: Pagination single page

	When I searched for item "cake" with "3" results and was redirected to the Results page
	Then there should be no pagination
	
Scenario: Pagination multi-page and visit second page

	When I searched for item "pizza" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I navigate to the second page
	Then I am on the "Results for pizza" page

Scenario: Test table hover on clickable results

	When I searched for item "korean" with "800" radius and was redirected to the Results page
	Then there should be table-hover