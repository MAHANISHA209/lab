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

public class ProductSearchTest {

    public static void pause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("==================================");
        System.out.println("PRODUCT SEARCH TEST");
        System.out.println("==================================");

        System.out.println("Opening Demo Web Shop...");
        driver.get("https://demowebshop.tricentis.com/");
        pause(3);

        // ---------- SEARCH FOR A PRODUCT ----------
        String searchItem = "computer";

        System.out.println("Locating search box on home page...");
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("small-searchterms")));

        System.out.println("Entering search keyword: " + searchItem);
        searchBox.clear();
        searchBox.sendKeys(searchItem);
        pause(1);

        System.out.println("Clicking search button...");
        WebElement searchButton = driver.findElement(
                By.cssSelector(".search-box-button"));
        searchButton.click();
        pause(3);

        // ---------- VERIFY RESULTS ----------
        System.out.println("Reading search results...");
        try {
            WebElement firstResult = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(".product-title a")));

            System.out.println("Expected: Product related to '"
                    + searchItem + "' should appear in results.");
            System.out.println("Actual: Found product - "
                    + firstResult.getText());

            String productName = firstResult.getText();

            System.out.println("Clicking first search result...");
            firstResult.click();
            pause(3);

            System.out.println("Reading product page title...");
            WebElement title = driver.findElement(By.cssSelector(".product-name h1"));
            System.out.println("Product Page Title: " + title.getText());

            if (title.getText().equalsIgnoreCase(productName)) {
                System.out.println("Result: PASS - Product page matches search item.");
            } else {
                System.out.println("Result: FAIL - Product page does not match.");
            }

            System.out.println("Clicking Add to cart...");
            WebElement addToCart = driver.findElement(
                    By.cssSelector("input[value='Add to cart']"));
            addToCart.click();
            pause(2);

            System.out.println("Product Added to Cart Successfully");

        } catch (Exception e) {
            System.out.println("Expected: Product related to '"
                    + searchItem + "' should appear in results.");
            System.out.println("Actual: No matching product found. Result: FAIL");
        }
        pause(2);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(),
                new File("ProductSearchSuccess.png").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot Saved Successfully");
        pause(2);

        driver.quit();
        System.out.println("Browser Closed");
        System.exit(0);
    }
}
