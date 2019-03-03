package vip.ifmm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import vip.ifmm.helper.HashHelper;

import java.util.UUID;

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
