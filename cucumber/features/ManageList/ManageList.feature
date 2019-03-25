Feature:

	Manage List Page with search for Chinese and add/move/remove Chinese Chicken Fried Rice II

Background:

	Given I searched for "Chinese"
	And I clicked the link for "Chinese Chicken Fried Rice II"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button

Scenario: Check text size, title text size (F1, F2)

	Then I should see a title
	And I should see a result in default text size
	
Scenario: Select a list item (RTPF7a.1, LMPF1, LMPF2)
	
	Then I should see "Chinese Chicken Fried Rice II" on the page
	When I clicked the link for "name: Chinese Chicken Fried Rice II"
	Then I am on the "Chinese Chicken Fried Rice II" page
	
Scenario: Remove an item (LMPF3.1)

	When I remove "Chinese Chicken Fried Rice II"
	Then I selected "To Explore" from the drop down
	And I clicked the "Manage Lists" button
	And I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	And I do not see "Chinese Chicken Fried Rice II"
	
Scenario: Move an item (LMPF3.2, LMPF6, LMPF7)
	
	When I selected "To Explore" from the drop down
	And I move "Chinese Chicken Fried Rice II"
	And I selected "To Explore" from the drop down
	And I clicked the "Manage Lists" button
	And I am on the "To Explore" page
	And I should see "Chinese Chicken Fried Rice II" on the page
	And I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	And I am on the "Favorites" page
	And I do not see "Chinese Chicken Fried Rice II"
	
Scenario: Go back to results page (LMPF4)

	When I clicks the "Back to Results" button
	Then I am on the "Results for Chinese" page

Scenario: Go back to search page (LMPF5)

	When I clicks the "Back to Search" button
	Then I am on the "I'm Hungry" page

Scenario: Manage lists with no list selected (LMPF7a)

	And I click the "Manage Lists" button with nothing selected
	Then I am on the "Favorites" page
