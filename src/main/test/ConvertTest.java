import org.junit.Test;
import prs.whyat.util.Converter;

/**
 * @ClassName: ConvertTest
 * @Description: 测试类
 * @Author QiuBo
 * @Date 2019/4/18 20:18
 */

public class ConvertTest {
    //测试转化短链
    @Test
    public void m01(){
        String shortCode = Converter.shorten(1,6);
        System.out.println(shortCode);
    }
}
