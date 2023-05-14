package com.example.avocado.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class dictUnitTest {
    @Test
    public void dictTest()
    {
        dict dict1=new dict("abc");
        assertEquals(dict1.getTitle(),"abc");

        dict dict2=new dict("abc");
        assertNotEquals(dict2.getDictID(),dict1.getDictID());
        assertEquals(dict2.getTitle(),dict1.getTitle());

        dict dict3=new dict("new");
        assertNotEquals(dict2.getDictID(),dict1.getDictID());
        assertNotEquals(dict1.getTitle(),dict3.getTitle());

        System.out.println(dict3.getDictID()+ " "+dict3.getTitle()+" "+dict3.getDate());
    }
}
