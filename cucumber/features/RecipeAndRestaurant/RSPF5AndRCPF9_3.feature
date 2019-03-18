Feature:

	Restaurant Page Functionality 5 and Recipe Page Functionality 9 with search for Pad Thai and selection of for Trio House and Pad Thai Egg Rolls

Background:

	Given I searched for item "Pad Thai" with "4" results and was redirected to the Results page
	And I clicked the link for "Trio House"
	And I selected "Do Not Show" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I clicked the link for "Pad Thai Egg Rolls"
	And I selected "Do Not Show" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I select "Do Not Show" from the drop down
	And I clicked the "Manage Lists" button
	
Scenario: Add to do not show list (RCPF9.2, RCPF5.2)

	Then I should see "Pad Thai Egg Rolls" on the page
	Then I should see "Trio House" on the page
