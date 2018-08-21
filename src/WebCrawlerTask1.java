import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
// we need to extract link with :
// departure airport
//arrival airport
//departure date from 2018-09-01---2018-09-30
//fligth type only direct
//30 departure days
//no restrictions about prices, airports and time

public class WebCrawlerTask1 {

    private final Set<URL> links;

    private WebCrawlerTask1(final URL startURL) {
        this.links = new HashSet<>();
        crawl(initURLS(startURL));
    }

    private static Set<URL> initURLS(final URL startURL) {
        final Set<URL> startURLS = new HashSet<>();
        startURLS.add(startURL);
        return startURLS;
    }

    //code to filter out the url which had already been checked
    private void crawl(final Set<URL> URLS) {
        URLS.removeAll(this.links);
        if (!URLS.isEmpty()) {
            final Set<URL> newURLS = new HashSet<>();
            try {
                this.links.addAll(URLS);
                for (final URL url : URLS) {
                    System.out.println(" connect to : " + url);
                    final Document document = Jsoup.connect(url.toString()).get();
                    //extracting the data regarding request
                    final Elements departure_city = document.select("a[D_City=OSLALL]");
                    final Elements arrival_city = document.select("a[A_City=RIX]");
                    final Elements flight_type = document.select("a[TripType=1]");
                    final Elements departure_day = document.select("a[D_Day=02]");
                    final Elements departure_month = document.select("a[D_Month=201809]");

                    //loop to load the pages and extract the variables//
                    for (final Element page : departure_city) {
                        final String urlText = page.attr("D_City=OSLALL").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);

                    }
                    for (final Element page : arrival_city) {
                        final String urlText = page.attr("A_City=RIX").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);
                    }
                    for (final Element page : flight_type) {
                        final String urlText = page.attr("TripType=1").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);
                    }
                    for (final Element page : departure_day) {
                        final String urlText = page.attr("D_Day=02").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);

                    }
                    for (final Element page : departure_month) {
                        final String urlText = page.attr("D_Month=201809").trim();
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);

                    catch( final Exception |Error ignored){
                        }
                        crawl(newURLS);
                    }
                }

                private void writeResults () throws IOException {
                    final File tmpFile = File.createTempFile("crawlResults", ".out");
                    try (final FileWriter writer = new FileWriter(tmpFile)) {
                        for (final URL url : this.links) {
                            writer.write(url + "\n");
                        }
                    }
                }
//making url request/response filtering, to sort out the information which is not needed.
                //
                public static void main (String[]args) throws IOException {
                    final WebCrawler webCrawler = new WebCrawler(new URL("https://https://www.norwegian.com /us/ipc/availability/avaday?" + "D_City=OSLALL{request.departure_city}" + "&A_City=RIX{request.arrival_city}" + "&TripType=1{request.trip_type}" + "&D_Day=02{request.d_day}" + "&D_Month=201809{request.d_month}&D_SelectedDay=02" + "&R_Day=02{response.d_day}" + "&R_Month=201809{response.d_month}" + "&R_SelectedDay=02&IncludeDirect=true&AgreementCodeFK=-1&CurrencyCode=USD&rnd=73473&processid=62621&mode=ab"));
                    webCrawler.writeResults();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
