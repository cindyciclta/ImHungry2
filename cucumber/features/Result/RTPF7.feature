Feature:

	Results Page Functionality 7 with search for Pizza and addition of Pizza Studio to Do Not Show and Hummus Pizza to Favorites

Background:

	Given I searched for item "Pizza" with "7" results and was redirected to the Results page
	And I clicked the link for "Pizza Studio"
	And I selected "Do Not Show" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	And I clicked the link for "Hummus Pizza"
	And I selected "Favorites" from the drop down
	And I clicked the "Add to List" button
	And I clicks the "Back to Results" button
	
Scenario: Add items to lists and have results change appropriately (RTPF7b, RTPF7c, RTPF7a)

	Then I should see "Hummus Pizza" as the first result for "Recipes"
	And I do not see "Pizza Studio"

