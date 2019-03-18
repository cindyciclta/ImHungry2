Feature:

	Restaurant Page Functionality 5 and Recipe Page Functionality 9 with search for Cereal and selection of for Milk Tavern and So Pink Cereal Bars

Background:

	Given I searched for item "Cereal" with "4" results and was redirected to the Results page
	And I clicked the link for "Milk Tavern"
	And I selected "To Do" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I clicked the link for "So Pink Cereal Bars"
	And I selected "To Do" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I select "To Do" from the drop down
	And I clicked the "Manage Lists" button
	
Scenario: Add to to do list (RCPF9.2, RCPF5.2)

	Then I should see "So Pink Cereal Bars" on the page
	Then I should see "Milk Tavern" on the page
