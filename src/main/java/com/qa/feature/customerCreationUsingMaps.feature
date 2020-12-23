Feature: Creation of multiple Customers in Netsuite 

Scenario: Test scenario for multiple customer creation 

	Given User login with "sravan.k@tvarana.com" & "Tvarana@2020" 
	Then Create customers with the below data & save the customer 
		|     Type   | First name | Last name | Company name | Parent Company |
		| INDIVIDUAL |    Tom     |    Smith  |   Microsoft  | Sravan K |
		| COMPANY    |            |           |   Deloite    | Sravan K |
	Then close the browser