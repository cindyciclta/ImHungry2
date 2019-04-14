Feature:

	Test new features related to list management added in project 2 (list persistence)
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"
	
Scenario: Test list persistence for favorites list

	When I searched for item "cake" with "6" results and was redirected to the Results page
	And I clicked the link for "Golden Rum Cake"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I click the sign out button
	And I searched for item "cake" with "6" results and was redirected to the Results page
	Then there should be "Golden Rum Cake" as the first recipe
	
	When I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	Then I should see "Golden Rum Cake" on the page
	
	When I clicked the link for "name: Golden Rum Cake"
	Then I am on the "Golden Rum Cake" page
	
	When I clicks the "Back to Results" button
	And I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	And I remove "Golden Rum Cake"
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "cake" with "6" results and was redirected to the Results page
	And I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	Then I do not see "Golden Rum Cake"
	
Scenario: Test list persistence for To Explore list

	When I searched for item "mexican" with "6" results and was redirected to the Results page
	And I clicked the link for "El Cholo"
	And I selected "To Explore" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "mexican" with "6" results and was redirected to the Results page
	Then I should see "El Cholo" on the page
	
	When I selected "To Explore" from the drop down
	And I clicked the "Manage Lists" button
	Then I should see "El Cholo" on the page
	
	When I clicked the link for "name: El Cholo"
	Then I am on the "El Cholo" page
	
	When I clicks the "Back to Results" button
	And I selected "To Explore" from the drop down
	And I clicked the "Manage Lists" button
	And I remove "El Cholo"
	And I clicks the "Back to Results" button
	And I click the sign out button
	And I searched for item "mexican" with "6" results and was redirected to the Results page
	And I selected "To Explore" from the drop down
	And I clicked the "Manage Lists" button
	Then I do not see "El Cholo"