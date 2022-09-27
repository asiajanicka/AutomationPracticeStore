package pageObjects.categoryPages;

import enums.SortProductsOptions;
import io.qameta.allure.Step;
import lombok.Getter;
import org.apache.commons.collections.map.LinkedMap;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.base.BasePage;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoryPage extends BasePage {

    public CategoryPage(WebDriver driver) {
        super(driver);
        baseWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-heading")));
        categoriesLeftBlock = new CategoriesLeftBlock(driver);
    }

    @FindBy(css = ".category-name")
    private  WebElement categoryName;
    @FindBy(css = ".cat_desc>div.rte")
    private WebElement categoryDesc;
    @FindBy(css = ".page-heading>.cat-name")
    private WebElement pageHeader;
    @FindBy(css = ".heading-counter")
    private WebElement productCounter;
    @FindBy(css = "#subcategories>ul>li a.subcategory-name")
    private List<WebElement> subcategories;
    @FindBy(css = "#subcategories")
    private WebElement subcategoriesBlock;
    private final String paginationPattern = "^[a-zA-Z]* ([1-9]+) - ([1-9]+) of ([1-9]+) item[s]?";
    @FindBy(css = ".product-count")
    private List<WebElement> pagination;
    @FindBy(css = ".ajax_block_product")
    private List<WebElement> products;
    @Getter
    private CategoriesLeftBlock categoriesLeftBlock;
    @FindBy(css = "#enabled_filters>ul>li")
    private List<WebElement> enabledFilters;
    @FindBy(css = "#ul_layered_category_0>li")
    private List<WebElement> categoriesFilterItems;
    @FindBy(css = "#ul_layered_id_attribute_group_1>li")
    private List<WebElement> sizeFilterItems;
    @FindBy(css = "#ul_layered_id_attribute_group_3>li")
    private List<WebElement> colorFilterItems;
    @FindBy(css = "#ul_layered_id_feature_7>li")
    private List<WebElement> propertiesFilterItems;
    @FindBy(css = "#ul_layered_id_feature_5>li")
    private List<WebElement> compositionsFilterItems;
    @FindBy(css = "#ul_layered_id_feature_6>li")
    private List<WebElement> stylesFilterItems;
    @FindBy(css = "#ul_layered_quantity_0>li")
    private List<WebElement> availabilityFilterItems;
    @FindBy(css = "#ul_layered_condition_0>li")
    private List<WebElement> conditionFilterItems;
    @FindBy(id = "layered_price_range")
    private WebElement priceRangeLabel;
    @FindBy(css = ".ui-slider-handle")
    private List<WebElement> sliderHandlers;
    private final String loaderLocator = ".product_list>p>img";
    @FindBy(className = "product_list")
    private WebElement productList;
    @FindBy(id = "selectProductSort")
    private WebElement sortProductDpd;
    @FindBy(css = ".product_list .alert-warning")
    private WebElement noProductsAlert;

    public String getPageHeader() {
        return pageHeader.getText().strip();
    }
    public String getCategoryName(){
        return categoryName.getText().strip();
    }
    public String getCategoryDesc(){
        return categoryDesc.getText().strip();
    }

    public Map<String, WebElement> getEnabledFilters(){
        String patternStr = "[A-Z][a-z]+: (.*)";
        Pattern pattern = Pattern.compile(patternStr);
        Map<String ,WebElement> filters = new HashMap<>();
        for(WebElement filter : enabledFilters){
            Matcher matcher = pattern.matcher(filter.getText().strip());
            if(matcher.matches())
            filters.put(matcher.group(1).strip(),
                    filter.findElement(By.cssSelector("a[title='Cancel']"))
                    );
        }
        return filters;
     }

     @Step("Disable filter with value: {0}")
     public CategoryPage disableFilterByValue(String value){
        getEnabledFilters().get(value).click();
         try {
             WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
             baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(products.get(0))));
         } catch (TimeoutException e){
         }

         return this;
     }
    @Step("Sort products by: {0}")
     public CategoryPage sortProductsBy(SortProductsOptions value){
        Select select = new Select(sortProductDpd);
        select.selectByValue(value.toString());
        return this;
    }

    public String getSelectedSortByOption(){
        Select select = new Select(sortProductDpd);
        return select.getFirstSelectedOption().getAttribute("value");
    }

    public List<CategoryProductPage> getProducts() {
        List productsParsed = new ArrayList<CategoryProductPage>();

        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(products.get(0))));
        } catch (TimeoutException e){

        }
        for (WebElement product : products)
            productsParsed.add(new CategoryProductPage(driver, product));
        return productsParsed;
    }

    public int getNumberOfProductsFromProductCounter() {
        String text = productCounter.getText();
        if(text.toLowerCase().contains("no products")){
            return 0;
        } else {
            return Integer.parseInt(productCounter.getText().replaceAll("\\D", ""));
        }
    }

    public List<String> getSubcategoriesNames() {
        ArrayList<String> subcategoriesNames = new ArrayList<>();
        for (WebElement subcategory : subcategories) {
            subcategoriesNames.add(subcategory.getText());
        }
        return subcategoriesNames;
    }

    public boolean areSubcategoriesDisplayed(){
        return isElementDisplayed(subcategoriesBlock);
    }

    public boolean isPaginationDisplayed(){
        return isElementDisplayed(pagination.get(0)) ||
                isElementDisplayed(pagination.get(1));
    }

    public int getFirstNumberFromPagination(){
        return getNumberFromPagination(1);
    }

    public int getLastNumberFromPagination(){
        return getNumberFromPagination(2);
    }
    public int getTotalNumberFromPagination(){
        return getNumberFromPagination(3);
    }

    private int getNumberFromPagination(int groupId){
        Pattern pattern = Pattern.compile(paginationPattern);
        Matcher matcher = pattern.matcher(pagination.get(0).getText().strip());
        if(matcher.matches())
            return Integer.parseInt(matcher.group(groupId));
        else throw new IllegalArgumentException("There is no such pattern group");
    }

    // FILTERS
    // CATEGORIES FILTER
    public boolean isCategoriesFilterDisplayed() {
        return categoriesFilterItems.size()>0;
    }

    public Map<String, Integer> getCategoriesFromFilter() {
        LinkedMap categories = new LinkedMap();
        for (WebElement el : categoriesFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String size = text.replaceAll("[^a-zA-Z-\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            categories.put(size, amount);
        }
        return categories;
    }

    @Step("Select category filter with {0}")
    public CategoryPage selectCategoryFilterByName(String name) {
        for (WebElement el : categoriesFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z-\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(categoriesFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }

    // SIZE FILTER
    public Map<String, Integer> getSizesFromFilter() {
        LinkedMap sizes = new LinkedMap();
        for (WebElement el : sizeFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String size = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            sizes.put(size, amount);
        }
        return sizes;
    }
    @Step("Select size filter with {0}")
    public CategoryPage selectSizeFilterByName(String name) {
        for (WebElement el : sizeFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(sizeFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }

    // COLOR FILTER
    public Map<String,Integer> getColorsFromFilter() {
        LinkedMap colors = new LinkedMap();
        for (WebElement el : colorFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String color = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            colors.put(color, amount);
        }
        return colors;
    }

    @Step("Select color filter with {0}")
    public CategoryPage selectColorFilterByName(String name) {
        for (WebElement el : colorFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
               scrollToElement(el);
                el.findElement(By.cssSelector("label>a")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(colorFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }
    // PROPERTIES FILTER
    public Map<String,Integer> getPropertiesFromFilter() {
        LinkedMap properties = new LinkedMap();
        for (WebElement el : propertiesFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String color = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            properties.put(color, amount);
        }
        return properties;
    }
    @Step("Select property filter with {0}")
    public CategoryPage selectPropertyFilterByName(String name) {
        for (WebElement el : propertiesFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(propertiesFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }
    // COMPOSITIONS FILTER
    public Map<String,Integer> getCompositionsFromFilter() {
        LinkedMap compositions = new LinkedMap();
        for (WebElement el : compositionsFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String color = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            compositions.put(color, amount);
        }
        return compositions;
    }
    @Step("Select compositions filter with {0}")
    public CategoryPage selectCompositionsFilterByName(String name) {
        for (WebElement el : compositionsFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(compositionsFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }

    // STYLES FILTER
    public Map<String,Integer> getStylesFromFilter() {
        LinkedMap styles = new LinkedMap();
        for (WebElement el : stylesFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String color = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            styles.put(color, amount);
        }
        return styles;
    }
    @Step("Select style filter with {0}")
    public CategoryPage selectStylesFilterByName(String name) {
        for (WebElement el : stylesFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(stylesFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }

    // PRICE FILTER
    public String getPriceRangeFromLabel(){
        return priceRangeLabel.getText().strip();
    }
    public BigDecimal getLowPriceRangeFromLabel(){
        int end = priceRangeLabel.getText().indexOf(" -");
        return new BigDecimal(priceRangeLabel.getText().substring(1, end).strip());
    }
    public BigDecimal getHighPriceRangeFromLabel(){
        int beginning = priceRangeLabel.getText().indexOf("-")+3;
        return new BigDecimal(priceRangeLabel.getText().substring(beginning).strip());
    }

    public CategoryPage setLowPriceOnFilterSlider(BigDecimal priceRange){
        if(getLowPriceRangeFromLabel().compareTo(priceRange)>0) {
            while (getLowPriceRangeFromLabel().compareTo(priceRange)> 0) {
                sliderHandlers.get(0).sendKeys(Keys.ARROW_LEFT);
            }
        }else if(getLowPriceRangeFromLabel().compareTo(priceRange)<0){
            while (getLowPriceRangeFromLabel().compareTo(priceRange)<0) {
                sliderHandlers.get(0).sendKeys(Keys.ARROW_RIGHT);
            }
        }
        return this;
    }

    @Step("Set high price slider on {0}")
    public CategoryPage setHighPriceOnFilterSlider(BigDecimal priceRange){
        if(getHighPriceRangeFromLabel().compareTo(priceRange)<0) {
            while (getHighPriceRangeFromLabel().compareTo(priceRange)< 0) {
                sliderHandlers.get(1).sendKeys(Keys.ARROW_RIGHT);
            }
        }else if(getHighPriceRangeFromLabel().compareTo(priceRange)>0){
            while (getHighPriceRangeFromLabel().compareTo(priceRange)>0) {
                sliderHandlers.get(1).sendKeys(Keys.ARROW_LEFT);
            }
        }
        return this;
    }

    // AVAILABILITY FILTER
    public Map<String, Integer> getAvailabilityFromFilter() {
        LinkedMap availability = new LinkedMap();
        for (WebElement el : availabilityFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String size = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            availability.put(size, amount);
        }
        return availability;
    }

    @Step("Select in stock filter")
    public CategoryPage selectInStockFilter(){
        for (WebElement el : availabilityFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase("In stock")) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        return this;
    }

    // CONDITION FILTER
    public Map<String, Integer> getConditionFromFilter() {
        LinkedMap condition = new LinkedMap();
        for (WebElement el : conditionFilterItems) {
            String text = el.findElement(By.cssSelector("label>a"))
                    .getText();
            String size = text.replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            int amount = Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
            condition.put(size, amount);
        }
        return condition;
    }

    @Step("Select condition filter with {0}")
    public CategoryPage selectConditionFilter(String name){
        for (WebElement el : conditionFilterItems) {
            String label = el.findElement(By.cssSelector("label>a"))
                    .getText()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .strip();
            if (label.equalsIgnoreCase(name)) {
                el.findElement(By.className("checker")).click();
                break;
            }
        }
        try {
            WebDriverWait baseWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            baseWait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(conditionFilterItems.get(0))));
        } catch (TimeoutException e){
        }
        return this;
    }

    public boolean isNoProductsAlertDisplayed(){
        boolean isDisplayed = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        try {
            wait.until(ExpectedConditions.visibilityOf(noProductsAlert));
            isDisplayed = true;
        } catch (Exception e){

        }
        return isDisplayed;
    }

    public boolean isNoProductsAlertRemoved(){
        boolean isRemoved = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(ExpectedConditions.invisibilityOf(noProductsAlert));
            isRemoved = true;
        } catch (Exception e){

        }
        return isRemoved;
    }
}
