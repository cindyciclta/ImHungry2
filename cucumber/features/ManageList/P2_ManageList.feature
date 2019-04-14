Feature:

	Test new features related to list management added in project 2 (list persistence)
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"
	
Scenario: Test that list item remains in favorites after signing out and back in

	When I searched for item "cake" with "6" results and was redirected to the Results page
	And I clicked the link for "Golden Rum Cake"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I click the sign out button
	And I search for item "cake" with "6" results and was redirected to the Results page
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	Then I should see "Golden Rum Cake" on the page
	
	When I clicked the link for "name: Golden Rum Cake"
	Then I am on the "Golden Rum Cake" page
	
	When I clicks the "Back to Results" button
	And I selected "Favorites" from the dropdown
	And I clicked the "Manage Lists" button
	And I remove "Golden Rum Cake"
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I search for item "cake" with "6" results and was redirected to the Results page
	And I selected "Favorites" from the dropdown
	And I clicked the "Manage Lists" button
	Then I do not see "Golden Rum Cake"