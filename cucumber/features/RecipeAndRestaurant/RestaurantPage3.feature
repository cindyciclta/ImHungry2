Feature:

	Restaurant Page with search for Dessert and details for Dulce

Background:
	Given I searched for "Dessert"
	And I clicked the link for "Dulce"

Scenario: Check background color, text size, title text size, appropriate fields (F1, F2, RSPF1)

	Then I should see background color "rgba(245, 245, 245, 1)"
	And I should see "Dulce" as a title
	And I should see a result in default text size
	And I should see the name, address, phone number, and link for the restaurant
	
Scenario: Address redirection (RSPF1a)

	When I click on the address
	Then I should be on Google Maps directions page for Dulce
	
Scenario: Website link redirection (RSPF1b)

	When I click on the website link
	Then I should be on the restaurant's home page, "/biz/dulce-los-angeles?adjust_creative=kOJ4-HptgaXNFbfpVFbrJg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=kOJ4-HptgaXNFbfpVFbrJg"
	
Scenario: Check for printable version (RSPF2)

	Then I should be able to print
	
Scenario: Go to results page (RSPF3)
	
	When I clicks the "Back to Results" button
	Then I am on the "Results for Dessert" page
	
Scenario: Check for drop down option (RSPF4)
	
	Then I should see a drop down box for lists
	
Scenario: Add a restaurant to a list (RSPF5.1)
	
	When I select "Favorites" from the drop down
	And I click the "Add to List" button
	Then I am on the "Dulce" page
