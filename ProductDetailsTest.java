import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetailsTest {
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
        System.out.println("PRODUCT DETAILS TEST");
        System.out.println("==================================");

        String username = "dharani" + System.currentTimeMillis();
        String password = "Test@1234";

        System.out.println("Opening SauceDemo...");
        driver.get("https://www.demoblaze.com/");
        pause(5);

        // ---------- SIGN UP ----------
        System.out.println("Clicking Sign up link...");
        driver.findElement(By.id("signin2")).click();
        pause(2);

        System.out.println("Entering new Username: " + username);
        driver.findElement(By.id("sign-username")).sendKeys(username);
        pause(1);

        System.out.println("Entering new Password...");
        driver.findElement(By.id("sign-password")).sendKeys(password);
        pause(1);

        System.out.println("Clicking Sign up button...");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        pause(2);

        try {
            Alert signupAlert = driver.switchTo().alert();
            System.out.println("Signup Alert: " + signupAlert.getText());
            signupAlert.accept();
        } catch (Exception e) {
            System.out.println("No signup alert appeared");
        }
        pause(2);

        // ---------- LOGIN ----------
        System.out.println("Clicking Log in link...");
        driver.findElement(By.id("login2")).click();
        pause(2);

        System.out.println("Entering Username...");
        driver.findElement(By.id("loginusername")).sendKeys(username);
        pause(1);

        System.out.println("Entering Password...");
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        pause(1);

        System.out.println("Clicking Log in button...");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        pause(4);

        try {
            String welcomeText = driver.findElement(By.id("nameofuser")).getText();
            System.out.println("Login Successful. " + welcomeText);
        } catch (Exception e) {
            System.out.println("Login Failed");
        }
        pause(2);

        // ---------- PRODUCT DETAILS ----------
        System.out.println("Waiting for MacBook air to appear...");
        WebElement macbookLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("MacBook air")));

        System.out.println("Scrolling to MacBook air...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", macbookLink);
        pause(1);

        System.out.println("Clicking on MacBook air...");
        macbookLink.click();
        pause(3);

        System.out.println("Reading Product Name...");
        WebElement name = driver.findElement(By.tagName("h2"));
        System.out.println("Product Name: " + name.getText());
        pause(1);

        System.out.println("Reading Product Price...");
        WebElement price = driver.findElement(By.className("price-container"));
        System.out.println("Product Price: " + price.getText());
        pause(1);

        System.out.println("Clicking Add to cart...");
        driver.findElement(By.xpath("//a[text()='Add to cart']")).click();
        pause(2);

        try {
            Alert cartAlert = driver.switchTo().alert();
            System.out.println("Alert says: " + cartAlert.getText());
            cartAlert.accept();
            System.out.println("Product Added to Cart Successfully");
        } catch (Exception e) {
            System.out.println("No confirmation alert appeared");
        }
        pause(3);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(), new File("ProductDetailsSuccess.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot Saved Successfully");
        pause(3);

        driver.quit();
        System.out.println("Browser Closed");
        System.exit(0);
    }
}
