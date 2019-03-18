Feature: Search Page

Background:

	Given I am on the search page

Scenario: check background color

	When I remain on the search page
	Then I should see background color white smoke

Scenario: RF2

	When I remain on the search page
	Then I should see prompt text enter food


