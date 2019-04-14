Feature:

	Test new features related to list management added in project 2 (list persistence)
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"
	
Scenario: Test list persistence for favorites list

	When I searched for item "cake" with "6" results and was redirected to the Results page
	And I clicked the link for "Golden Rum Cake"
	Then I am on the "Golden Rum Cake" page
	
	When I selected "Favorites" from the drop down
	And I add it to the list
	And I clicks the "Back to Results" button
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I click the sign out button
	And I searched for item "cake" with "6" results and was redirected to the Results page
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I selected "Favorites" from the drop down
	And I manage the list
	Then I should see "Golden Rum Cake" on the page
	
	When I clicked the link for "name: Golden Rum Cake"
	Then I am on the "Golden Rum Cake" page
	
	When I clicks the "Back to Results" button
	And I selected "Favorites" from the drop down
	And I manage the list
	And I remove "Golden Rum Cake"
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "cake" with "6" results and was redirected to the Results page
	And I selected "Favorites" from the drop down
	And I manage the list
	Then I do not see "Golden Rum Cake"
	
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
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "mexican" with "6" results and was redirected to the Results page
	And I selected "To Explore" from the drop down
	And I manage the list
	Then I do not see "Broken Spanish"
	
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
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "ice cream" with "6" results and was redirected to the Results page
	Then I should see "Super Lemon Ice Cream" on the page
	
	When I selected "Do Not Show" from the drop down
	And I manage the list
	Then I do not see "Super Lemon Ice Cream"