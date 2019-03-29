package com.mylnikov.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.yarn.annotation.OnContainerStart;
import org.springframework.yarn.annotation.YarnComponent;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;

/**
 * Search logic component.
 * Spring yarn component which invokes {@link #findMostPopularHotels()} after submit and shows result into logger.
 */
@YarnComponent
public class FindTask {

    private static final Log log = LogFactory.getLog(FindTask.class);

    /**
     * Hotel aggregation map.
     * Key is composite hotel key and value is count.
     */
    private Map<String, Long> hotelCountMap;

    @Autowired
    private Configuration configuration;

    /**
     * File name to parse.
     */
    @Value("${filePath}")
    private String filePath;

    /**
     * Reads input file line by line and aggregates the hotels.
     */
    @OnContainerStart
    public void findMostPopularHotels() throws Exception {
        log.info("FindTask started");
        init();
        log.info("Getting file:" + filePath);
        Path path = new Path("/" + filePath);
        LineReader lineReader = new LineReader(path.getFileSystem(configuration).open(path), (byte[]) null);
        log.info("File found");
        while (true) {
            Text input = new Text();
            lineReader.readLine(input);
            if(input.toString() == null || input.getLength()==0) {
                break;
            }
            parseLine(input);
        }
        showTop3Hotels();
    }

    void init() {
        hotelCountMap = new TreeMap<>();
    }

    boolean isCouple(String value) {
        try {
            return Integer.parseInt(value) == 2;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses line and increment {@link #hotelCountMap} if there is couple
     * @param line input line
     */
    void parseLine(Text line) {
        String[] values = line.toString().split(",");
        if (values.length != 24) {
            log.error("Error line has length " + values.length);
            return;
        }
        if(!isCouple(values[13])) {
            return;
        }
        String hotelKey = getHotelKey(values);
        if (hotelCountMap.containsKey(hotelKey)) {
            hotelCountMap.put(hotelKey, hotelCountMap.get(hotelKey) + 1L);
        } else {
            hotelCountMap.put(hotelKey, 1L);
        }
    }

    /**
     * Composite key is hotel country + hotel market.
     */
    String getHotelKey(String[] values) {
        return values[19] + ":" + values[20];
    }

    /**
     * Sorts {@link #hotelCountMap}.
     * @return top 3 most popular hotels
     */
    List<Map.Entry<String, Long>> getTop3Hotels() {
        return hotelCountMap.entrySet().stream().sorted(comparingByValue()).limit(3).collect(
                toList());
    }

    private void showTop3Hotels() {
        log.info("Showing top 3 hotels between couples");
       getTop3Hotels().forEach(entry -> log.info("Hotel:"+entry.getKey()));
    }

}
