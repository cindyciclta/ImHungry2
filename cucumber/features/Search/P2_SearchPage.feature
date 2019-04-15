Feature:

	Project 2 tests for new Search Page features
	
Background:

	Given I am on the search page

Scenario: Check default radius

	And I should see a text box to enter search radius
	And the default radius should be 5

Scenario: Check negative radius value

	When I enter a negative radius
	Then the text box should not accept the value
	
Scenario: Check bad radius value

	When I searched for item "Chihhjlhjklhjklhjkhjkhkjlhj;js" with "1" radius
	Then the page should have an error message
	
Scenario: Remain on search page if radius is too small
	
	When I searched for item "Indian" with "0.05" radius
	Then I should see a text box to enter search radius
	
Scenario: Use enter to search

	When I type "pizza" and I press enter
	Then I am on the "Results for pizza" page
