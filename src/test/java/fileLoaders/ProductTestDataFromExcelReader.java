package fileLoaders;

import enums.Stock;
import lombok.Getter;
import model.Product;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductTestDataFromExcelReader {

    @Getter
    private List<Product> products;

    public ProductTestDataFromExcelReader() {
        products = new ArrayList<>();
        loadData();
    }
    private void loadData() {
        FileInputStream fis;
        XSSFWorkbook wb;
        try {
            fis = new FileInputStream(new File("C:\\Users\\asiaj\\Desktop\\tests\\DaneTestowe.xlsx"));
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("There is no such file");
        } catch (IOException e) {
            throw new RuntimeException("File could not be loaded");
        }

        XSSFSheet sheet = wb.getSheetAt(0);
        for(int r=1; r<= sheet.getLastRowNum(); r++){
            XSSFRow row = sheet.getRow(r);
            String name = row.getCell(0).toString();
            String category = row.getCell(1).toString();
            String subcategory1stLevel = row.getCell(2).toString();
            String subcategory2ndLevel = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
            String reference = row.getCell(4).toString();
            String condition = row.getCell(5).toString();
            String color = row.getCell(6).toString();
            Stock availability = Stock.valueOf(row.getCell(7).toString());
            String sizes = row.getCell(8).toString();
            BigDecimal price = new BigDecimal(row.getCell(9).toString());
            BigDecimal oldPrice = checkOldPrice(row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
            String priceReduction = row.getCell(11,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
            String compositions = row.getCell(12).toString();
            String styles = row.getCell(13).toString();
            String properties = row.getCell(14).toString();
            products.add(new Product(name,
                    category,
                    subcategory1stLevel,
                    subcategory2ndLevel,
                    reference,
                    condition,
                    color,
                    availability,
                    sizes,
                    price,
                    oldPrice,
                    priceReduction,
                    compositions,
                    styles,
                    properties));
        }
    }

    private BigDecimal checkOldPrice(String price) {
        if(!price.isBlank()){
            return new BigDecimal(price);
        } else return BigDecimal.ZERO;
    }
}
