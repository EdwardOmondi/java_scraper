import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test {
    public static void main(String[] args) {
        String url = "https://www.mon-maire.fr/maire-de-abbans-dessous-25";
        Document mayorPage;
        try {
            mayorPage = org.jsoup.Jsoup.connect(url).get();
        } catch (Exception e) {
            return;
        }
        String dateOfTakingOffice = getDateOfTakingOffice(mayorPage);
        System.out.println("Test.main:\ndateOfTakingOffice = " + dateOfTakingOffice);
        String telephoneNumber = getTelephoneNumber(mayorPage);
        System.out.println("Test.main:\ntelephoneNumber = " + telephoneNumber);
        String address = getAddress(mayorPage);
        System.out.println("Test.main:\naddress = " + address);
    }

    private static String getTelephoneNumber(Document mayorPage) {
        Element telephoneElement = mayorPage.select("span[itemprop=telephone]").first();
        return telephoneElement.text().substring(1);
    }

    private static String getDateOfTakingOffice(Document mayorPage) {
        String dateSearchText = "a pris ses fonctions en tant que maire le ";
        String rawText = mayorPage.getElementsContainingOwnText(dateSearchText).text();
        int startingIndex = rawText.indexOf("a pris ses fonctions en tant que maire le ");
        return rawText.substring(startingIndex + dateSearchText.length(), startingIndex + dateSearchText.length() + 10);
    }

    private static String getAddress(Document page) {
        Element addressElement = page.select("span[itemscope][itemtype=\"https://schema.org/CityHall\"]").first();
        assert addressElement != null;
        return addressElement.text();
    }
}
