require 'capybara'
require 'capybara/cucumber'
require "selenium/webdriver"
require 'rspec'


Capybara.register_driver :chrome do |app|
  Capybara::Selenium::Driver.new(app, :browser => :chrome, :driver_path=>"/usr/local/bin/chromedriver")
end

Capybara.register_driver :headless_chrome do |app|
  capabilities = Selenium::WebDriver::Remote::Capabilities.chrome(
    chromeOptions: { args: %w(headless disable-gpu) }
  )

  Capybara::Selenium::Driver.new app,
    browser: :chrome,
    driver_path: "/usr/local/bin/chromedriver",
    desired_capabilities: capabilities
end

Capybara.default_driver = :headless_chrome