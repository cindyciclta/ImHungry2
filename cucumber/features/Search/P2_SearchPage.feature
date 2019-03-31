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
	
Scenario: Use enter to search

	When I type "chocolate" and I press enter
	Then I am on the "Results for chocolate" page

Scenario: Check for search history text

	Then there should be search history
	
Scenario: Check most recent search in search history

	When I type "soup" and I press enter
	And I clicks the "Return to Search" button
	Then there should be "soup" in the first search history

Scenario: Check second most recent search in search history

	When I type "salad" and I press enter
	And I clicks the "Return to Search" button
	And I type "ceviche" and I press enter
	And I clicks the "Return to Search" button
	Then there should be "ceviche" in the first search history
	And there should be "salad" in the second search history

Scenario: Check that search history is empty on another account

	When I have navigated to the sign up page
	And I sign up with username "testSearchHist" and password "searchHistPW"
	And I sign in with username "testSearchHist" and password "searchHistPW"
	Then the search history should be empty