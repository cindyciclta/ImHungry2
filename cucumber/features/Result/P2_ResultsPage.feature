Feature:

	Project 2 tests for new Results Page features
	
Background:

	Given I have navigated to the sign up page
	And I sign up with username "test" and password "test"

Scenario: Pagination single page

	When I searched for item "cake" with "3" results and was redirected to the Results page
	Then there should be no pagination
	
Scenario: Pagination multi-page does not have previous buttons on the first pages

	When I searched for item "korean" with "15" results and was redirected to the Results page
	Then there should be pagination
	And there should be no previous recipe button
	And there should be no previous restaurant button
	
Scenario: Pagination multi-page and visit second restaurant page

	When I searched for item "pizza" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I navigate to the second restaurant page
	Then I am on the "Results for pizza" page

Scenario: Pagination multi-page and visit second recipe page

	When I searched for item "burger" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I navigate to the second recipe page
	Then I am on the "Results for burger" page
	
Scenario: Pagination multi-page and use the next recipe button

	When I searched for item "indian" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I press the next recipe button
	Then I am on the "Results for indian" page

Scenario: Pagination multi-page and use the next restaurant button

	When I searched for item "thai" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I press the next restaurant button
	Then I am on the "Results for thai" page
	
Scenario: Pagination multi-page and use the previous recipe button

	When I searched for item "indian" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I navigate to the second recipe page
	And I press the previous recipe button
	Then I am on the "Results for indian" page
	
Scenario: Pagination multi-page and use the previous restaurant button

	When I searched for item "thai" with "15" results and was redirected to the Results page
	Then there should be pagination
	When I navigate to the second restaurant page
	And I press the previous restaurant button
	Then I am on the "Results for thai" page
	
Scenario: Pagination page marker stays in the middle

	When I searched for item "burger" with "35" results and was redirected to the Results page
	Then there should be pagination
	When I press the next restaurant button
	And I press the next restaurant button
	And I press the next restaurant button
	Then there should be a restaurant page "2" link
	And there should be a restaurant page "3" link
	And there should be a restaurant page "5" link
	And there should be a restaurant page "6" link

Scenario: Test table hover on clickable results

	When I searched for item "korean" with "3" radius and was redirected to the Results page
	Then there should be table-hover
	
Scenario: Check for search history text

	When I searched for item "donut" with "1" radius and was redirected to the Results page
	Then there should be search history
	
Scenario: Check most recent search in search history

	When I searched for item "soup" with "5" results and was redirected to the Results page
	And I clicks the "Return to Search" button
	And I type "cake" and I press enter
	Then there should be "soup" in the first search history
	
Scenario: Check that mini collages appear in search history

	When I searched for item "fries" with "4" results and was redirected to the Results page
	And I clicks the "Return to Search" button
	And I type "oatmeal" and I press enter
	Then there should be a mini collage in the search history

Scenario: Check two most recent searches in search history

	When I searched for item "salad" with "5" results and was redirected to the Results page
	And I clicks the "Return to Search" button
	And I type "ceviche" and I press enter
	And I clicks the "Return to Search" button
	And I type "cake" and I press enter
	Then there should be "ceviche" in the first search history
	And there should be "salad" in the second search history
	
Scenario: Test that consecutive duplicate searches only show up once in search history

	When I searched for item "french" with "5" results and was redirected to the Results page
	And I searched for item "burger" with "5" results and was redirected to the Results page
	And I searched for item "burger" with "5" results and was redirected to the Results page
	And I searched for item "cake" with "2" results and was redirected to the Results page
	Then there should be "burger" in the first search history
	And there should be "french" in the second search history
	
Scenario: Search for recent search by clicking on search history link

	When I searched for item "chicken" with "5" results and was redirected to the Results page
	And I clicks the "Return to Search" button
	And I type "cake" and I press enter
	Then there should be "chicken" in the first search history
	When I click the first search history link
	Then I am on the "Results for chicken" page

Scenario: Check that search history is separate for each account

	When I have navigated to the sign up page
	And I sign up with username "testSearchHist" and password "searchHistPW"
	And I sign in with username "testSearchHist" and password "searchHistPW"
	And I type "diffUser" and I press enter
	Then the search history should be empty or first search history should say "diffUser"
	
Scenario: Check color contrast for result table cells

	When I searched for item "donut" with "5" results and was redirected to the Results page
	Then the results should be high-contrast