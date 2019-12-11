import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected static int getRandomNumber(int min, int max) {
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

}
