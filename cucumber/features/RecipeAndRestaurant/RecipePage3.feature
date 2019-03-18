Feature:

	Recipe Page with search for Breakfast and details for Fluffy Pancakes

Background:
	Given I searched for "Breakfast"
	And I clicked the link for "Fluffy Pancakes"

Scenario: Check background color, text size, title text size, image, title (F1, F2, RCPF1, RCPF2)

	Then I should see background color "rgba(245, 245, 245, 1)"
	And I should see "Fluffy Pancakes" as a title
	And I should see a result in default text size
	And I should see an image
	
Scenario: Check prep time, cook time, ingredients, instructions (RCPF3, RCPF4, RCPF5)
	
	Then I should see the prep and cook time of the dish
	And I should see the ingredients of the dish
	And I should see instructions for the dish
	
Scenario: Check for printable version (RCPF6)
	
	Then I should be able to print
	
Scenario: Go to results page (RCPF7)
	
	When I clicks the "Back to Results" button
	Then I am on the "Results for Breakfast" page
	
Scenario: Check for drop down option (RCPF8)
	
	Then I should see a drop down box for lists
	
Scenario: Add an option to a list (RCPF9.1)
	
	When I select "Favorites" from the drop down
	And I click the "Add to List" button
	Then I am on the "Fluffy Pancakes" page
