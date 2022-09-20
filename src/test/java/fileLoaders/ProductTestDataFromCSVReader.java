package fileLoaders;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import pageObjects.base.Product;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProductTestDataFromCSVReader {

    @Getter
    private List<Product> products;
    private String filePath = "src/test/resources/ProductsTestData.csv";
    public ProductTestDataFromCSVReader() {
        products = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        Reader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(String.format("There is no file %s", filePath));
        }

        CsvToBean<Product> csvToBean = new CsvToBeanBuilder(reader)
                .withType(Product.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        products = csvToBean.parse();
    }
}
