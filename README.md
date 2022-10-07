The project contains Selenium tests written in Java for chosen components of fake clothing store http://automationpractice.pl/

The components covered by tests are as follows:
- contact us form
- carousel slider on home page
- tabs "Popular" items (with alert) and "Best sellers" (with products) on home page
- dropdown shopping cart on home page
- top menu
- product category page

Tests are designed with use of Page Object Model with composition approach and with Page Factory concept.

The project is created using following frameworks and libraries:
- Junit5 - for testing
- Allure - for reporting (allure results are stored in target/allure-results directory)
- AssertJ - for assertion
- Awaitility
- Lombok

Test can be run with use of CHROME and FIREFOX. 
In order to choose browser, go to config.properties and enter CHROME or FIREFOX for browser param.
To use mentioned browsers, they should be installed on your computer.


To serve test report with Allure on your computer:
1) run tests using: mvn clean test 
2) generate a report with: allure serve target/allure-results
Please remember that Allure must be installed according to the spec: https://docs.qameta.io/allure/contactUsForm