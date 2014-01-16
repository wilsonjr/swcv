package edu.webapp.server.db;

import edu.webapp.shared.WordCloud;

import java.util.Calendar;
import java.util.List;

/**
 * @author spupyrev
 * Jan 14, 2014
 */
public class DBTests
{
    public static void main(String[] args)
    {
        //DBUtils.createDB();
        //testAddCloud();
        testListClouds();
        //testCount();
    }

    private static void testAddCloud()
    {
        WordCloud wc = new WordCloud();
        //int id = new Random().nextInt(1234567);
        int id = DBUtils.getCloudCount();
        wc.setId(id);
        wc.setInputText("text");
        wc.setHeight(800);
        wc.setWidth(600);
        wc.setCreationDateAsDate(Calendar.getInstance().getTime());
        wc.setSvg("<svg /> ><g style='fill:white; stroke:white;' /></g");
        wc.setCreatorIP("127.1.3.4");

        DBUtils.addCloud(wc);

        WordCloud wcNew = DBUtils.getCloud(id);
        assert (wc.getId() == wcNew.getId());
        assert (wc.getCreationDate() == wcNew.getCreationDate());
    }

    private static void testListClouds()
    {
        List<WordCloud> clouds = DBUtils.getLatestClouds(25);
        System.out.println("size: " + clouds.size());
    }

    private static void testCount()
    {
        System.out.println("size: " + DBUtils.getCloudCount());
    }

}
