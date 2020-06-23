/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hoangng.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author popem
 */
public class TesterWithURL {

    public static void main(String[] args) throws IOException {
        String[] urls = {
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=2",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=3",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=4",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=5",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=6",
//            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=7",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=8",
            "https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=9", //            "https://www.comicextra.com/popular-comic/7"
        };

        for (String url : urls) {
            testWellformed(url);
        }
    }

    private static void testWellformed(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.setReadTimeout(16 * 1000);
        connection.setConnectTimeout(8 * 1000);

        String textContent = getString(connection.getInputStream());

        textContent = TextUtils.refineHtml(textContent);

        printToFile(textContent, Character.toString(urlString.charAt(urlString.length() - 1)));

        if (checkWellformedXml(textContent)) {
            System.out.println(urlString + " đã well-formed.");
        }
    }

    private static String getString(InputStream stream) {

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            if (stream != null) {
                System.out.println("close");
                stream.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private static String applyXpath(String doc) {
        InputSource inputXML = new InputSource(new StringReader(doc));

        XPath xPath = XPathFactory.newInstance().newXPath();

        String result = "";
        try {
            result = xPath.evaluate("/employees/employee/firstName", inputXML);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static void printToFile(String content, String name) {
        try {
            String fileName = name + ".html";
            File myObj = new File("./" + fileName);
            if (myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(content);
                System.out.println("written");
                myWriter.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static boolean checkWellformedXml(String src) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return false;
        }

        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());

            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }
        });

        try {
            builder.parse(new ByteArrayInputStream(src.getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }
}
