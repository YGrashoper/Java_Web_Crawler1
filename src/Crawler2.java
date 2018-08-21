import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
// we need to extract :
// departure airport
//arrival airport
//connection airport
//departure time
//arrival time
//cheapest prices
// taxes
//departure date 2018-09-03
//returning date 2018-09-09


public class Crawler2 {

    private final Set<URL> links;


    private Crawler2(final URL startURL) {
        this.links = new HashSet<>();
        crawl(initURLS(startURL));
    }

    private static Set<URL> initURLS(final URL startURL) {
        final Set<URL> startURLS = new HashSet<>();
        startURLS.add(startURL);
        return startURLS;
    }

    private void crawl(final Set<URL> URLS) {
        URLS.removeAll(this.links);
        if (!URLS.isEmpty()) {
            final Set<URL> newURLS = new HashSet<>();
            try {
                this.links.addAll(URLS);
                for (final URL url : URLS) {
                    System.out.println(
                            " connect to : " + url);
                    final Document document = Jsoup.connect(url.toString()).get();
                    final Elements linksOnPage = document.select("a[href]");
                    for (final Element page : linksOnPage) {
                        final String urlText = page.attr("abs:href").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);
                    }
                }
            } catch (final Exception | Error ignored) {
            }
            crawl(newURLS);
        }
    }

    private void writeResults() throws IOException {
        final File tmpFile = File.createTempFile("crawlResults", ".out");
        try(final FileWriter writer = new FileWriter(tmpFile)) {
            for(final URL url : this.links) {
                writer.write(url + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final Crawler2 crawler = new Crawler2(new URL("https://book.flysas.com/pl/SASC/wds/Override.action?SO_SITE_EXT_PSPURL=https://classic.sas.dk/SASCredits/SASCreditsPaymentMaster.aspx&SO_SITE_TP_TPC_POST_EOT_WT=50000&SO_SITE_USE_ACK_URL_SERVICE=TRUE&WDS_URL_JSON_POINTS=ebwsprod.flysas.com%2FEAJI%2FEAJIService.aspx&SO_SITE_EBMS_API_SERVERURL=%20https%3A%2F%2F1aebwsprod.flysas.com%2FEBMSPointsInternal%2FEBMSPoints.asmx&WDS_SERVICING_FLOW_TE_" +
                "SEATMAP=TRUE&WDS_SERVICING_FLOW_TE_XBAG=TRUE&WDS_SERVICING_FLOW_TE_MEAL=TRUE"));
        crawler.writeResults();
    }

}