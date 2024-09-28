
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;
import javax.lang.model.util.Elements;

public class Main {

    public static void main(String[] args) {

        // Fetch HTML and CSS content
        try {
            Document document = Jsoup.connect(website).get();

            // Extract the HTML content
            String htmlContent = document.html();

            // Extract and display important features from HTML
            parseHtmlFeatures(document);

            // Extract CSS stylesheet links
            Elements cssLinks = document.select("link[rel=stylesheet]");
            StringBuilder cssContent = new StringBuilder();
            for (Element link : cssLinks) {
                String cssUrl = link.absUrl("href"); // Get absolute URL of the CSS file
                cssContent.append(fetchCSSContent(cssUrl)).append("\n");
            }

            // Parse and display important features from the CSS content
            parseCssFeatures(cssContent.toString());

            // Save the data to MySQL database
            saveToDatabase(username, password, website);

        } catch (IOException e) {
            System.err.println("Error fetching the page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to fetch CSS content from a single CSS URL
    private static String fetchCSSContent(String cssUrl) {
        StringBuilder css = new StringBuilder();
        try {
            URL url = new URL(cssUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                css.append(inputLine).append("\n");
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Error fetching CSS: " + e.getMessage());
            e.printStackTrace();
        }
        return css.toString();
    }

    // Method to save username, password, and website URL in MySQL database
    private static void saveToDatabase(String username, String password, String website_url) {
        String dbpath = "jdbc:mysql://localhost:3306/wss";
        String dbUser = "root";
        String dbPassword = "ShauRu44";

        try (Connection connection = DriverManager.getConnection(dbpath, dbUser, dbPassword)) {
            String sql = "INSERT INTO web_data (username, password, website_url) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, website_url);
            statement.executeUpdate();

            System.out.println("Data successfully saved to the database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to parse and output important HTML features
    // Method to parse and output important HTML features
    private static void parseHtmlFeatures(Document document) {
        System.out.println("\nHTML features:");

        // Count occurrences of various HTML elements
        Elements divElements = document.select("div");
        Elements imgElements = document.select("img");
        Elements anchorElements = document.select("a");
        Elements paragraphElements = document.select("p");
        Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");
        Elements listElements = document.select("ul, ol");
        Elements listItemElements = document.select("li");
        Elements formElements = document.select("form");
        Elements inputElements = document.select("input");
        Elements buttonElements = document.select("button");
        Elements tableElements = document.select("table");
        Elements tableRowElements = document.select("tr");
        Elements tableDataElements = document.select("td, th");

        // Extract metadata information
        String title = document.title();
        Elements metaDescription = document.select("meta[name=description]");
        Elements metaKeywords = document.select("meta[name=keywords]");

        System.out.println("Number of <div> elements: " + divElements.size());
        System.out.println("Number of <img> elements: " + imgElements.size());
        System.out.println("Number of <a> (links) elements: " + anchorElements.size());
        System.out.println("Number of <p> (paragraph) elements: " + paragraphElements.size());
        System.out.println("Number of header elements (<h1> to <h6>): " + headerElements.size());
        System.out.println("Number of list elements (<ul>, <ol>): " + listElements.size());
        System.out.println("Number of list item elements (<li>): " + listItemElements.size());
        System.out.println("Number of form elements: " + formElements.size());
        System.out.println("Number of input elements: " + inputElements.size());
        System.out.println("Number of button elements: " + buttonElements.size());
        System.out.println("Number of table elements: " + tableElements.size());
        System.out.println("Number of table row elements (<tr>): " + tableRowElements.size());
        System.out.println("Number of table data elements (<td>, <th>): " + tableDataElements.size());
        System.out.println("Page title: " + title);
        if (!metaDescription.isEmpty()) {
            System.out.println("Meta description: " + metaDescription.attr("content"));
        }
        if (!metaKeywords.isEmpty()) {
            System.out.println("Meta keywords: " + metaKeywords.attr("content"));
        }
    }

    // Method to parse and output important CSS features
    private static void parseCssFeatures(String cssContent) {
        System.out.println("\nCSS features:");

        // CSS properties
        int colorCount = countOccurrences(cssContent, "color:");
        int fontSizeCount = countOccurrences(cssContent, "font-size:");
        int marginCount = countOccurrences(cssContent, "margin:");
        int paddingCount = countOccurrences(cssContent, "padding:");
        int displayCount = countOccurrences(cssContent, "display:");
        int backgroundCount = countOccurrences(cssContent, "background:");
        int widthCount = countOccurrences(cssContent, "width:");
        int heightCount = countOccurrences(cssContent, "height:");
        int positionCount = countOccurrences(cssContent, "position:");
        int fontFamilyCount = countOccurrences(cssContent, "font-family:");
        int mediaQueryCount = countOccurrences(cssContent, "@media");

        System.out.println("Number of 'color' properties: " + colorCount);
        System.out.println("Number of 'font-size' properties: " + fontSizeCount);
        System.out.println("Number of 'margin' properties: " + marginCount);
        System.out.println("Number of 'padding' properties: " + paddingCount);
        System.out.println("Number of 'display' properties: " + displayCount);
        System.out.println("Number of 'background' properties: " + backgroundCount);
        System.out.println("Number of 'width' properties: " + widthCount);
        System.out.println("Number of 'height' properties: " + heightCount);
        System.out.println("Number of 'position' properties: " + positionCount);
        System.out.println("Number of 'font-family' properties: " + fontFamilyCount);
        System.out.println("Number of '@media' queries: " + mediaQueryCount);
    }

    // Method to count occurrences of css features
    private static int countOccurrences(String content, String searchString) {
        int count = 0;
        int index = 0;
        while ((index = content.indexOf(searchString, index)) != -1) {
            count++;
            index += searchString.length();
        }
        return count;
    }
}
