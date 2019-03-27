package com.mylnikov.container;

import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class FindTaskTest {

    private FindTask findTask = new FindTask();

    @Before
    public void init() {
        findTask.init();
    }

    @Test
    public void testIsCoupleMethod() {
        Assert.assertEquals(true, findTask.isCouple("2"));
        Assert.assertEquals(false, findTask.isCouple("1"));
        Assert.assertEquals(false, findTask.isCouple("asfdsfsdf"));
    }

    @Test
    public void testGetHotelKey() {
        String[] values = new String[24];
        values[19] = "13";
        values[20] = "3";
        Assert.assertEquals("13:3", findTask.getHotelKey(values));
    }

    @Test
    public void testLineParsing() {
        String[] values = new String[24];
        values[19] = "13";
        values[20] = "3";
        values[13] = "2";;
        Text text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "13";
        values[20] = "3";
        values[13] = "2";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "13";
        values[20] = "3";
        values[13] = "2";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "13";
        values[20] = "9";
        values[13] = "1";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "9";
        values[20] = "3";
        values[13] = "2";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "9";
        values[20] = "1";
        values[13] = "2";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        values[19] = "9";
        values[20] = "1";
        values[13] = "2";;
        text = new Text(String.join(",", values));
        findTask.parseLine(text);

        List<Map.Entry<String, Long>> top3Hotels = findTask.getTop3Hotels();
        Assert.assertEquals("13:3", top3Hotels.get(2).getKey());
        Assert.assertEquals("9:1", top3Hotels.get(1).getKey());
        Assert.assertEquals("9:3", top3Hotels.get(0).getKey());

    }

}
