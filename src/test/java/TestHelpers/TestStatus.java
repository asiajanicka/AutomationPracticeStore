package TestHelpers;

import lombok.Getter;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Getter
public class TestStatus implements AfterTestExecutionCallback {
    public boolean isFailed;
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        if(extensionContext.getExecutionException().isPresent()){
            isFailed = true;
        } else {
            isFailed = false;
        }
    }
}
