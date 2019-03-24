Feature:

	Results Page with search for Indian and 10 results

Background:

	Given I searched for item "Indian" with "10" results and was redirected to the Results page

Scenario: Check background color, text size, title text size, collage, title, drop down (F1, F2, RTPF1, RTPF2, RTPF3.1)

	Then I should see background color "rgba(245, 245, 245, 1)"
	And I should see a title
	And I should see a result in default text size
	And I should see an image
	And I should see the title "Results for Indian"
	And I should see a drop down box with nothing selected
	
Scenario: Select a list (RTPF3.2)
	
	When I click the drop down box
	Then I should be able to select one of the predefined lists

Scenario: Manage list page redirection (RTPF4.2)

	When I selected "Favorites" from the drop down
	And I clicked the "Manage Lists" button
	Then I am the "Favorites" page

Scenario: Manage list page redirection with no selection (RTPF4a)

	When I click the "Manage Lists" button with nothing selected
	Then I am on the "Results for Indian" page

Scenario: Check title, column titles, number of results, appropriate fields (RTPF5a, b, RTPF6a,b)

	Then I am on the "Results for Indian" page
	And I should see a column with title "Recipes"
	And I should see "10" recipes
	And I should see the name, stars, and prep time for the recipes
	And I should see a column with title "Restaurants"
	And I should see "10" restaurants
	And I should see the name, address, stars, driving minutes, and price range for the restaurants

Scenario: Select a restaurant (RTPF5c)

	When I clicked the link for "House of Curry"
	Then I am on the "House of Curry" page
	
Scenario: Select a recipe (RTPF6c)

	When I clicked the link for "Indian Chai Hot Chocolate"
	Then I am on the "Indian Chai Hot Chocolate" page
	
Scenario: Go to search page (RTPF8)

	When I clicks the "Return to Search" button
	Then I am on the "I'm Hungry" page

Scenario: No results because small radius
	
	When I searched for item "Indian" with "3" radius and was redirected to the Results page
	Then I should not see any restaurants