Feature:

	Results Page with search for Green Beans and 12 results

Background:

	Given I searched for item "Green Beans" with "12" results and was redirected to the Results page

Scenario: Check text size, title text size, collage, title, drop down (F1, F2, RTPF1, RTPF2, RTPF3.1)

	Then I should see a title
	And I should see a result in default text size
	And I should see an image
	And I should see the title "Results for Green Beans"
	And I should see a drop down box with nothing selected
	
Scenario: Select a list (RTPF3.2)
	
	When I click the drop down box
	Then I should be able to select one of the predefined lists

Scenario: Manage list page redirection (RTPF4.2)

	When I selected "Do Not Show" from the drop down
	And I clicked the "Manage Lists" button
	Then I am the "Do Not Show" page

Scenario: Manage list page redirection with no selection (RTPF4a)

	When I click the "Manage Lists" button with nothing selected
	Then I am on the "Results for Green Beans" page

Scenario: Check title, column titles, number of results, appropriate fields (RTPF5a, b, RTPF6a,b)

	Then I am on the "Results for Green Beans" page
	And I should see a column with title "Recipes"
	And I should see "12" recipes
	And I should see the name, stars, and prep time for the recipes
	And I should see a column with title "Restaurants"
	And I should see "12" restaurants
	And I should see the name, address, stars, driving minutes, and price range for the restaurants

Scenario: Select a restaurant (RTPF5c)

	When I clicked the link for "Dirt Dog"
	Then I am on the "Dirt Dog" page
	
Scenario: Select a recipe (RTPF6c)

	When I clicked the link for "Southern Green Beans"
	Then I am on the "Southern Green Beans" page
	
Scenario: Go to search page (RTPF8)

	When I clicks the "Return to Search" button
	Then I am on the "I'm Hungry" page
