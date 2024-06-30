package com.edward;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class Scraper extends Utilities {
    public static final String OUTPUT_FILE_NAME = "scraper.csv";
    private static final String URL = "https://www.mon-maire.fr/maires-regions";
    private static final String DATE_SEARCH_TEXT = "a pris ses fonctions en tant que maire le ";
    private int regionLimiter;
    private int mayorLimiter;
    private int pageLimiter;

    public Scraper(int regionLimiter, int mayorLimiter, int pageLimiter) {
        this.regionLimiter = regionLimiter;
        this.mayorLimiter = mayorLimiter;
        this.pageLimiter = pageLimiter;
        log(Level.INFO, "Scraper initialised with region limiter: " + regionLimiter + ", mayor limiter: " + mayorLimiter + " and page limiter: " + pageLimiter);
    }

    public Scraper() {
        this(100, 100, 50);
    }

    private static String getAddress(Document page) {
        Element addressElement = page.select("span[itemscope][itemtype=\"https://schema.org/CityHall\"]").first();
        assert addressElement != null;
        return addressElement.text();
    }

    public void scrape() {
        try (FileWriter fileWriter = new FileWriter(OUTPUT_FILE_NAME); CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Région", "Ville", "Nom du maire", "Date de prise de fonction", "URL", "Téléphone", "Email", "Adresse Mairie"))) {
            Document landingPage = Jsoup.connect(URL).get();
            Elements regions = landingPage.getElementsByClass("list-group-item");
            for (int regionIndex = 0; regionIndex < regions.size(); regionIndex++) {
                processRegion(csvPrinter, regions, regionIndex);
                if (regionIndex == regionLimiter - 1) break;
            }
        } catch (IOException e) {
            log(Level.SEVERE, "Error beginning scraping: " + e.getMessage());
            return;
        }
        log(Level.INFO, "Scraping complete.");
    }

    private void processRegion(CSVPrinter csvPrinter, Elements regions, int regionIndex) {
        String region = regions.get(regionIndex).text();
        log(Level.INFO, "\tRegion: " + region);
        String regionUrl = regions.get(regionIndex).getElementsByTag("a").attr("href");
        log(Level.INFO, "\t\tRegion URL: " + regionUrl);
        Document regionPage;
        try {
            regionPage = Jsoup.connect(regionUrl).get();
        } catch (IOException e) {
            log(Level.SEVERE, "Error processing region: " + e.getMessage());
            return;
        }
        String totalMayorPagesString = regionPage.getElementsByClass("page-numbers").get(regionPage.getElementsByClass("page-numbers").size() - 2).text();
        int totalMayorPages = Integer.parseInt(totalMayorPagesString);
        for (int currentPage = 1; currentPage <= totalMayorPages; currentPage++) {
            processPage(csvPrinter, regions, regionIndex, region, totalMayorPages, currentPage);
            if (currentPage == pageLimiter) break;
        }
    }

    private void processPage(CSVPrinter csvPrinter, Elements regions, int regionIndex, String region, int totalMayorPages, int currentPage) {
        Document regionPage;
        String regionUrl;
        log(Level.INFO, "\t\tProcessing page " + currentPage + " of " + totalMayorPages + " for region " + region);
        regionUrl = regions.get(regionIndex).getElementsByTag("a").attr("href") + "/page/" + currentPage;
        try {
            regionPage = Jsoup.connect(regionUrl).get();
        } catch (IOException e) {
            log(Level.SEVERE, "Error processing region page " + currentPage + ": " + e.getMessage());
            return;
        }
        Elements mayors = regionPage.getElementsByClass("list-group-item");
        for (int mayorIndex = 0; mayorIndex < mayors.size(); mayorIndex++) {
            processMayor(csvPrinter, region, mayors, mayorIndex);
            if (mayorIndex == mayorLimiter - 1) break;
        }
    }

    private void processMayor(CSVPrinter csvPrinter, String region, Elements mayors, int mayorIndex) {
        String city = mayors.get(mayorIndex).text().split(" - ")[0];
        String mayor = mayors.get(mayorIndex).getElementsByTag("a").text();
        String mayorUrl = mayors.get(mayorIndex).getElementsByTag("a").attr("href");
        log(Level.INFO, "\t\tCity: " + city + " Mayor: " + mayor);
        Document mayorPage;
        try {
            mayorPage = Jsoup.connect(mayorUrl).get();
        } catch (IOException e) {
            log(Level.SEVERE, "Error processing mayor: " + e.getMessage());
            return;
        }
        String dateOfTakingOffice = getDateOfTakingOffice(mayorPage);
        String phone = getTelephoneNumber(mayorPage);
        String email = getEmail(mayorPage);
        String address = getAddress(mayorPage);
        log(Level.INFO, "\t\t\tDate of taking office: " + dateOfTakingOffice);
        log(Level.INFO, "\t\t\tURL: " + mayorUrl);
        log(Level.INFO, "\t\t\tPhone: " + phone);
        log(Level.INFO, "\t\t\tEmail: " + email);
        log(Level.INFO, "\t\t\tAddress: " + address);
        try {
            csvPrinter.printRecord(region, city, mayor, dateOfTakingOffice, mayorUrl, phone, email, address);
        } catch (IOException e) {
            log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    private String getDateOfTakingOffice(Document page) {
        String rawText = page.getElementsContainingOwnText(DATE_SEARCH_TEXT).text();
        int startingIndex = rawText.indexOf(DATE_SEARCH_TEXT);
        try {
            return rawText.substring(startingIndex + DATE_SEARCH_TEXT.length(), startingIndex + DATE_SEARCH_TEXT.length() + 10);
        } catch (Exception e) {
            return "";
        }
    }

    private String getTelephoneNumber(Document page) {
        Element element = page.select("span[itemprop=telephone]").first();
        if (element == null) return "";
        return element.text().substring(1);
    }

    private String getEmail(Document page) {
        Element element = page.select("span[itemprop=email]").first();
        if (element == null) return "";
        return element.text();
    }
}
