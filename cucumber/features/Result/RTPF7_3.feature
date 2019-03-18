Feature:

	Results Page Functionality 7 with search for Ice and addition of Anko to Do Not Show and Super Lemon  Cream to Favorites

Background:

	Given I searched for item "Ice" with "7" results and was redirected to the Results page
	And I clicked the link for "Anko"
	And I selected "Do Not Show" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I clicked the link for "Super Lemon Ice Cream"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	
Scenario: Add items to lists and have results change appropriately (RTPF7b, RTPF7c, RTPF7a)

	Then I should see "Super Lemon Ice Cream" as the first result for "Recipes"
	And I do not see "Anko"

