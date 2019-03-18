Feature:

	Restaurant Page Functionality 5 and Recipe Page Functionality 9 with search for Salad and selection of for Everytable and Harvest Salad

Background:

	Given I searched for item "Salad" with "4" results and was redirected to the Results page
	And I clicked the link for "Everytable"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I clicked the link for "Harvest Salad"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I select "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	
Scenario: Add to favorites list (RCPF9.2, RCPF5.2)

	Then I should see "Harvest Salad" on the page
	Then I should see "Everytable" on the page
