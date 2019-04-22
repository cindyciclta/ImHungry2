Feature:

	Test new features related to list management added in project 2 (list persistence)
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"
	
#Scenario: Test list persistence for Favorites list
#
#	When I searched for item "cake" with "6" results and was redirected to the Results page
#	And I clicked the link for "Golden Rum Cake"
#	Then I am on the "Golden Rum Cake" page
#	
#	When I selected "Favorites" from the drop down
#	And I add it to the list
#	And I clicks the "Back to Results" button
#	Then there should be "Golden Rum Cake" as the first recipe
#	
#	When I click the sign out button
#	And I searched for item "cake" with "6" results and was redirected to the Results page
#	Then there should be "Golden Rum Cake" as the first recipe
#	
#	When I selected "Favorites" from the drop down
#	And I manage the list
#	Then I should see "Golden Rum Cake" on the page
#	
#	When I clicked the link for "name: Golden Rum Cake"
#	Then I am on the "Golden Rum Cake" page
	
#	When I clicks the "Back to Results" button
#	And I selected "Favorites" from the drop down
#	And I manage the list
#	And I remove "Golden Rum Cake"
#	And I selected "Favorites" from the drop down
#	And I manage the list
#	Then I do not see "Golden Rum Cake"
#	
#	When I clicks the "Back to Results" button
#	And I click the sign out button
#	And I searched for item "cake" with "6" results and was redirected to the Results page
#	And I selected "Favorites" from the drop down
#	And I manage the list
#	Then I do not see "Golden Rum Cake"
	
Scenario: Test list persistence for To Explore list

	When I searched for item "mexican" with "6" results and was redirected to the Results page
	And I clicked the link for "Broken Spanish"
	Then I am on the "Broken Spanish" page
	
	When I selected "To Explore" from the drop down
	And I add it to the list
	And I clicks the "Back to Results" button
	Then I should see "Broken Spanish" on the page
	
	When I click the sign out button
	And I searched for item "mexican" with "6" results and was redirected to the Results page
	Then I should see "Broken Spanish" on the page
	
	When I selected "To Explore" from the drop down
	And I manage the list
	Then I should see "Broken Spanish" on the page
	
	When I clicked the link for "name: Broken Spanish"
	Then I am on the "Broken Spanish" page
	
	When I clicks the "Back to Results" button
	And I selected "To Explore" from the drop down
	And I manage the list
	And I remove "Broken Spanish"
	And I selected "To Explore" from the drop down
	And I manage the list
	Then I do not see "Broken Spanish"
	
	When I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "mexican" with "6" results and was redirected to the Results page
	And I selected "To Explore" from the drop down
	And I manage the list
	Then I do not see "Broken Spanish"
	
Scenario: Test Navigation to Grocery List from Manage List
	When I searched for item "ice cream" with "6" results and was redirected to the Results page
	When I selected "Do Not Show" from the drop down
	And I manage the list
	When I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page

Scenario: Test Navigation from Results Page to Manage List
	When I searched for item "ice cream" with "6" results and was redirected to the Results page
	When I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page

Scenario: Test Navigation from Grocery List to Grocery list
	When I searched for item "ice cream" with "6" results and was redirected to the Results page
	When I selected "Groceries" from the drop down
	And I manage the list
	When I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page
	
Scenario: Test list persistence for Do Not Show list

	When I searched for item "ice cream" with "6" results and was redirected to the Results page
	And I clicked the link for "Super Lemon Ice Cream"
	Then I am on the "Super Lemon Ice Cream" page
	
	When I selected "Do Not Show" from the drop down
	And I add it to the list
	And I clicks the "Back to Results" button
	Then I should not see "Super Lemon Ice Cream" on the page
	
	When I click the sign out button
	And I searched for item "ice cream" with "6" results and was redirected to the Results page
	Then I should not see "Super Lemon Ice Cream" on the page
	
	When I selected "Do Not Show" from the drop down
	And I manage the list
	Then I should see "Super Lemon Ice Cream" on the page
	
	When I clicked the link for "name: Super Lemon Ice Cream"
	Then I am on the "Super Lemon Ice Cream" page
	
	When I clicks the "Back to Results" button
	And I selected "Do Not Show" from the drop down
	And I manage the list
	And I remove "Super Lemon Ice Cream"
	And I selected "Do Not Show" from the drop down
	And I manage the list
	Then I do not see "Super Lemon Ice Cream"
	
	When I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "ice cream" with "6" results and was redirected to the Results page
	Then I should see "Super Lemon Ice Cream" on the page
	
	When I selected "Do Not Show" from the drop down
	And I manage the list
	Then I do not see "Super Lemon Ice Cream"
	
Scenario: Test persistent list re-ordering for Manage List

	When I searched for item "pasta" with "6" results and was redirected to the Results page
	And I clicked the link for "Creamy Cajun Chicken Pasta"
	Then I am on the "Creamy Cajun Chicken Pasta" page
	
	When I selected "Favorites" from the drop down
	And I add it to the list
	And I clicks the "Back to Results" button
	And I clicked the link for "Pasta Pomodoro"
	Then I am on the "Pasta Pomodoro" page
	
	When I selected "Favorites" from the drop down
	And I add it to the list
	And I clicks the "Back to Results" button
	And I selected "Favorites" from the drop down
	And I manage the list
	Then the first recipe in the list should be "Creamy Cajun Chicken Pasta"
	And the second recipe in the list should be "Pasta Pomodoro"

	When I drag the second recipe to the first recipe
	
	And I selected "Favorites" from the drop down
	And I manage the list
	
	Then there should be "Pasta Pomodoro" as the first recipe name
	And there should be "Creamy Cajun Chicken Pasta" as the second recipe name

Scenario: Test navigating to grocery list page

	When I searched for item "burger" with "3" results and was redirected to the Results page
	And I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page

Scenario: Test grocery list features

#Add first recipe's ingredients to grocery list

	When I searched for item "ecuadorian" with "6" results and was redirected to the Results page
	And I clicked the link for "Ecuadorian Seco de Pollo"
	Then I am on the "Ecuadorian Seco de Pollo" page
	When I add the "chicken" to the grocery list
	And I add the "red onion" to the grocery list
	And I add the "cilantro" to the grocery list
	
#duplicate ingredients added

	And I add the "chicken" to the grocery list
	And I add the "chicken" to the grocery list

#Add second recipe's ingredients to grocery list

	And I searched for item "coffee" with "6" results and was redirected to the Results page
	And I clicked the link for "Blueberry Sour Cream Coffee Cake"
	Then I am on the "Blueberry Sour Cream Coffee Cake" page
	When I add the "sour cream" to the grocery list
	And I add the "vanilla extract" to the grocery list
	And I add the "chopped pecans" to the grocery list
	
#Check that the ingredients are all displayed in the grocery list
	
	And I clicks the "Back to Results" button
	And I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page
	And I should see "chicken" on the page
	And there should only be one "chicken"
	And I should see "red onion" on the page
	And I should see "cilantro" on the page
	And I should see "sour cream" on the page
	And I should see "vanilla extract" on the page
	And I should see "chopped pecans" on the page
	
#Test that ingredients are persistent after signing out and back in
	
	When I clicks the "Back to Search" button
	And I click the sign out button
	And I searched for item "taco" with "3" results and was redirected to the Results page
	And I selected "Groceries" from the drop down
	And I manage the list
	Then I am on the "Grocery List" page
	And I should see "chicken" on the page
	And I should see "red onion" on the page
	And I should see "cilantro" on the page
	And I should see "sour cream" on the page
	And I should see "vanilla extract" on the page
	And I should see "chopped pecans" on the page
	
#Test removal of all but one ingredient
	
	#When I remove "chicken"
	#And I remove "red onion"
	#And I remove "cilantro"
	#And I remove "sour cream"
	#And I remove "vanilla extract"
	#And I clicks the "Back to Results" button
	#And I selected "Groceries" from the drop down
	#And I manage the list
	#Then I should not see "chicken" on the page
	#And I should not see "red onion" on the page
	#And I should not see "cilantro" on the page
	#And I should not see "sour cream" on the page
	#And I should not see "vanilla extract" on the page
	#And I should see "chopped pecans" on the page
	
#Test that remove ingredients remain remove after signing out and back in
	
	#When I clicks the "Back to Search" button
	#And I click the sign out button
	#And I searched for item "taco" with "4" results and was redirected to the Results page
	#And I selected "Groceries" from the drop down
	#And I manage the list
	#Then I am on the "Grocery List" page
	#And I should not see "red onion" on the page
	#And I should not see "cilantro" on the page
	#And I should not see "sour cream" on the page
	#And I should not see "vanilla extract" on the page
	#And I should see "chopped pecans" on the page
	#When I remove "chopped pecans"
	#Then I should not see "chopped pecans" on the page