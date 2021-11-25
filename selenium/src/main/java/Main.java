import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class Main {
    private static final String URL = "https://www.avito.ru/";
    private static final String CATEGORY_VALUE = "Оргтехника и расходники";
    private static final String SEARCH_VALUE = "Принтер";
    private static final String TOWN_VALUE = "Владивосток";
    private static final String FILTER_VALUE = "Дороже";
    private static final int COUNT_LIMIT = 3;

    public Main() {
    }

    public static void main(String[] args) throws InterruptedException {
        String chromePath = Main.class.getResource("./chromedriver.exe").getPath();
        System.setProperty("webdriver.chrome.driver", chromePath);
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.avito.ru/");
        Select categorySelect = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div/select")));
        WebElement category = (WebElement)categorySelect.getOptions().stream().filter((x) -> {
            return x.getAccessibleName().equals("Оргтехника и расходники");
        }).findFirst().get();
        category.click();
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[2]/div/div/div/label[1]/input")).sendKeys(new CharSequence[]{"Принтер"});
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[5]/div[1]/span/span/div")).click();
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[1]/div[2]/div/input")).sendKeys(new CharSequence[]{"Владивосток"});
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[1]/div[2]/div/ul/li[1]")).click();
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[3]/div[2]/div/button")).click();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", new Object[0]);
        WebElement delivery = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[1]/div/div[2]/div[1]/form/div[6]/div/div/div/div/div/div/label/span"));
        if (!delivery.isSelected()) {
            delivery.click();
        }

        driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[1]/div/div[2]/div[2]/div/button[1]")).click();
        Select filter = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[3]/div[1]/div[2]/select")));
        ((WebElement)filter.getOptions().stream().filter((x) -> {
            return x.getAccessibleName().equals("Дороже");
        }).findFirst().get()).click();
        List<String> names = (List)driver.findElements(By.xpath("//*[@class=\"iva-item-titleStep-_CxvN\"]/a/h3")).stream().map(WebElement::getText).limit(3L).collect(Collectors.toList());
        List<String> prices = (List)driver.findElements(By.xpath("//*[@class=\"iva-item-priceStep-QN8Kl\"]/span/span/meta[2]")).stream().limit(3L).map((x) -> {
            return x.getAttribute("content");
        }).collect(Collectors.toList());

        for(int i = 0; i < 3; ++i) {
            System.out.print((String)names.get(i));
            System.out.print(" : ");
            System.out.print((String)prices.get(i));
            System.out.println(" Р");
        }

        Thread.sleep(5000L);
        driver.quit();
    }
}
