require 'capybara'
require 'capybara/cucumber'
require "selenium/webdriver"
require 'rspec'


Capybara.register_driver :chrome do |app|
  Capybara::Selenium::Driver.new(app, :browser => :chrome, :driver_path=>"C:\\Users\\veda\\Downloads\\chromedriver.exe")
end

Capybara.register_driver :headless_chrome do |app|
  capabilities = Selenium::WebDriver::Remote::Capabilities.chrome(
    chromeOptions: { args: %w(headless disable-gpu window-size=1280,800) }
  )

  Capybara::Selenium::Driver.new app,
    browser: :chrome,
    driver_path: "/usr/local/bin/chromedriver",
    desired_capabilities: capabilities
end

Capybara.default_driver = :headless_chrome