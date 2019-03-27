Feature:

	Project 2 tests for new Search Page features
	
Background:

	Given I am on the search page

Scenario: Check default radius

	And I should see a text box to enter search radius
	And the default radius should be 1000

Scenario: Check negative radius value

	When I enter a negative radius
	Then the text box should not accept the value