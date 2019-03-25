Feature:

	Search Page

Background:

	Given I am on the search page

Scenario: text size (F1, F2)

	Then I should see a title

Scenario: Check prompt, text box, default value, button (SPF2, SPF3.1, SPF4)

	Then I should see prompt text enter food
	And I should see a text box to enter number of results
	And the default value should be 5
	And I should see a text box to enter search radius
	And the default radius should be 5
	And I should see a button labeled "Feed me!"

Scenario: Check negative search values (SPF3.2)

	When I enter a negative value
	Then the text box should not accept the value

Scenario: Check negative radius value

	When I enter a negative radius
	Then the text box should not accept the value
	
Scenario: Check over (SPF3.3)

	When I hover over the text box
	Then text should appear saying "Number of items to show in results"

Scenario: Redirect to results page for Chinese (SPF5)

	When I searched for "Chinese"
	Then I should be on the "/ImHungry/SearchPageController?action=search&term=Chinese&limit=5" page 
	
Scenario: Redirect to results page for Pancakes (SPF5)

	When I searched for "Pancakes"
	Then I should be on the "/ImHungry/SearchPageController?action=search&term=Pancakes&limit=5" page 
	
Scenario: Redirect to results page for Noodles (SPF5)

	When I searched for "Noodles"
	Then I should be on the "/ImHungry/SearchPageController?action=search&term=Noodles&limit=5" page