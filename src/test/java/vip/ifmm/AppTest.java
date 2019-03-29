package vip.ifmm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import cn.com.fishin.helper.HashHelper;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void testSimpleHash()
    {
        System.out.println(HashHelper.stringSimpleHash("", 16));
    }
}
