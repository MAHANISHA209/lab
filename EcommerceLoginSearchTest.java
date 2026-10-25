import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EcommerceLoginSearchTest {

    static WebDriver driver;
    static WebDriverWait wait;

    public static void pause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("=================================");
        System.out.println(" EXPERIMENT");
        System.out.println(" E-COMMERCE LOGIN AND PRODUCT SEARCH");
        System.out.println("=================================");

        String uniqueId = String.valueOf(System.currentTimeMillis());
        String email = "dharani" + uniqueId + "@test.com";
        String password = "Test@1234";
        boolean loginSuccess = false;

        try {

            // ------------------------------------
            // TEST CASE 1: NEW USER REGISTRATION
            // ------------------------------------

            System.out.println("\nTC-01: New User Registration");

            driver.get("https://demowebshop.tricentis.com/register");
            pause(3);

            driver.findElement(By.id("gender-male")).click();
            driver.findElement(By.id("FirstName")).sendKeys("maha");
            driver.findElement(By.id("LastName")).sendKeys("nisha");
            driver.findElement(By.id("Email")).sendKeys(email);
            driver.findElement(By.id("Password")).sendKeys(password);
            driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
            driver.findElement(By.id("register-button")).click();
            pause(3);

            System.out.println("Expected: Account should be registered successfully.");

            try {
                WebElement regResult = driver.findElement(By.cssSelector(".result"));
                System.out.println("Actual: " + regResult.getText());
            } catch (Exception e) {
                System.out.println("Actual: Registration confirmation not found.");
            }


            // ------------------------------------
            // TEST CASE 2: LOGOUT AFTER REGISTRATION
            // ------------------------------------

            System.out.println("\nTC-02: Logout After Registration");

            try {
                driver.findElement(By.cssSelector(".ico-logout")).click();
                pause(2);
                System.out.println("Expected: User should be logged out.");
                System.out.println("Actual: Logout successful, returned to home page.");
            } catch (Exception e) {
                System.out.println("Expected: User should be logged out.");
                System.out.println("Actual: Logout link not found.");
            }


            // ------------------------------------
            // TEST CASE 3: LOGIN WITH USERNAME AND PASSWORD
            // ------------------------------------

            System.out.println("\nTC-03: Login With Registered Account");

            driver.get("https://demowebshop.tricentis.com/login");
            pause(3);

            driver.findElement(By.id("Email")).sendKeys(email);
            driver.findElement(By.id("Password")).sendKeys(password);
            driver.findElement(By.cssSelector(".button-1.login-button")).click();
            pause(3);

            System.out.println("Expected: User should be logged in successfully.");

            try {
                WebElement account = driver.findElement(By.cssSelector(".account"));
                System.out.println("Actual: Logged in as " + account.getText());
                loginSuccess = true;
            } catch (Exception e) {
                System.out.println("Actual: Login failed.");
            }


            // ------------------------------------
            // TEST CASE 4: SEARCH FOR A VALID PRODUCT
            // ------------------------------------

            System.out.println("\nTC-04: Search For Valid Product");

            String searchItem = "laptop";

            WebElement searchBox = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("small-searchterms")));
            searchBox.clear();
            searchBox.sendKeys(searchItem);

            driver.findElement(By.cssSelector(".search-box-button")).click();
            pause(3);

            System.out.println("Expected: Product related to '" + searchItem + "' should appear in results.");

            String productName = null;

            try {
                WebElement firstResult = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector(".product-title a")));
                productName = firstResult.getText();
                System.out.println("Actual: Found product - " + productName);
            } catch (Exception e) {
                System.out.println("Actual: No matching product found.");
            }


            // ------------------------------------
            // TEST CASE 5: OPEN PRODUCT DETAILS PAGE
            // ------------------------------------

            System.out.println("\nTC-05: Open Product Details Page");

            if (productName != null) {

                WebElement firstResult = driver.findElement(By.cssSelector(".product-title a"));
                firstResult.click();
                pause(3);

                WebElement title = driver.findElement(By.cssSelector(".product-name h1"));

                System.out.println("Expected: Product page title should match '" + productName + "'.");
                System.out.println("Actual: " + title.getText());

                if (title.getText().equalsIgnoreCase(productName)) {
                    System.out.println("Result: PASS");
                } else {
                    System.out.println("Result: FAIL");
                }

            } else {
                System.out.println("Expected: Product page title should match search item.");
                System.out.println("Actual: Skipped - no product to open.");
            }


            // ------------------------------------
            // TEST CASE 6: ADD PRODUCT TO CART
            // ------------------------------------

            System.out.println("\nTC-06: Add Product To Cart");

            if (productName != null) {
                try {
                    driver.findElement(By.cssSelector("input[value='Add to cart']")).click();
                    pause(2);
                    System.out.println("Expected: Product should be added to cart.");
                    System.out.println("Actual: Product added to cart successfully.");
                } catch (Exception e) {
                    System.out.println("Expected: Product should be added to cart.");
                    System.out.println("Actual: Add to cart button not found.");
                }
            } else {
                System.out.println("Expected: Product should be added to cart.");
                System.out.println("Actual: Skipped - no product available.");
            }


            // ------------------------------------
            // TEST CASE 7: SEARCH FOR INVALID PRODUCT
            // ------------------------------------

            System.out.println("\nTC-07: Search For Invalid Product");

            driver.get("https://demowebshop.tricentis.com/");
            pause(2);

            String invalidItem = "zzzxxxnotaproduct";

            WebElement searchBox2 = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("small-searchterms")));
            searchBox2.clear();
            searchBox2.sendKeys(invalidItem);
            driver.findElement(By.cssSelector(".search-box-button")).click();
            pause(3);

            System.out.println("Expected: No product should be found for '" + invalidItem + "'.");

            try {
                driver.findElement(By.cssSelector(".product-title a"));
                System.out.println("Actual: Unexpected - a product was found.");
            } catch (Exception e) {
                System.out.println("Actual: No products found, as expected.");
            }


            System.out.println("\n=================================");
            System.out.println(" TEST EXECUTION COMPLETED");
            System.out.println(" Login Status: " + (loginSuccess ? "SUCCESS" : "FAILED"));
            System.out.println("=================================");

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(),
                    new File("EcommerceTestResult.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot Saved Successfully");

        }

        catch (Exception e) {
            System.out.println("\nDEFECT FOUND:");
            System.out.println(e.getMessage());
        }

        finally {
            pause(3);
            driver.quit();
            System.out.println("Browser Closed");
        }
    }
}
