import com.google.gdata.client.analytics.AnalyticsService
import com.google.gdata.data.analytics.AccountFeed
import com.google.gdata.data.analytics.AccountEntry
import com.google.gdata.client.analytics.DataQuery
import com.google.gdata.data.analytics.DataFeed
import com.google.gdata.data.analytics.DataEntry

AnalyticsService myService = new AnalyticsService("DanielsExample");
def username = this.args[0];
def password = this.args[1];
myService.setUserCredentials(username, password);

// something about account
URL feedUrl = new URL("https://www.google.com/analytics/feeds/accounts/default");
AccountFeed accountFeed = myService.getFeed(feedUrl, AccountFeed.class);
println accountFeed.getTitle().getPlainText();
for (AccountEntry entry : accountFeed.getEntries()) {
  println "\t${entry.getTitle().getPlainText()} : ${entry.getTableId().getValue()}";
}
println "*" * 20;

// get the first profile, for me, it's umcssa.info. and do something
AccountEntry profile = accountFeed.getEntries().get(1);
def tableId = profile.getTableId().getValue();

// basic query
DataQuery basicQuery = getBasicQuery(tableId);
DataFeed basicData = myService.getFeed(basicQuery, DataFeed.class);
printData("BASIC RESULTS", basicData);

// Ask Analytics to return the data sorted in descending order of visits
DataQuery sortedQuery = getBasicQuery(tableId);
sortedQuery.setSort("-ga:visits");
DataFeed sortedData = myService.getFeed(sortedQuery, DataFeed.class);
printData("SORTED RESULTS", sortedData);

// Ask Analytics to filter out browsers that contain the word "Explorer"
DataQuery filteredQuery = getBasicQuery(tableId);
filteredQuery.setFilters("ga:browser!@Explorer");
DataFeed filteredData = myService.getFeed(filteredQuery, DataFeed.class);
printData("FILTERED RESULTS", filteredData);

///////////////////////////////

def DataQuery getBasicQuery(String tableId){
  URL dataUrl = new URL("https://www.google.com/analytics/feeds/data");
  DataQuery query = new DataQuery(dataUrl);
  query.setIds(tableId);
  query.setStartDate("2009-07-01");
  query.setEndDate("2009-07-07");
  query.setDimensions("ga:browser");
  query.setMetrics("ga:visits,ga:bounces");
  return query;
}

def void printData(String title, DataFeed dataFeed) {
  println title;
  for (DataEntry entry : dataFeed.getEntries()) {
    browser = entry.stringValueOf("ga:browser");
    visits = entry.longValueOf("ga:visits");
    bounces = entry.longValueOf("ga:bounces");
    bounceRate = bounces / visits;
    println "\tBrowser:${browser}, Visits:${visits}, Bounces:${bounces}, BounceRate:${bounceRate}";
  }
  System.out.println();
}