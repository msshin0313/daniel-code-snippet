import com.google.gdata.client.analytics.AnalyticsService
import com.google.gdata.data.analytics.AccountFeed
import com.google.gdata.data.analytics.AccountEntry
import com.google.gdata.client.analytics.DataQuery
import com.google.gdata.data.analytics.DataFeed

AnalyticsService myService = new AnalyticsService("DanielsExample");
def username = this.args[0];
def password = this.args[1];
myService.setUserCredentials(username, password);

// search for the profile of drupal.org
URL feedUrl = new URL("https://www.google.com/analytics/feeds/accounts/default");
AccountFeed accountFeed = myService.getFeed(feedUrl, AccountFeed.class);
AccountEntry doProfile;
accountFeed.getEntries().each {
  if (it.getTitle().getPlainText().equals('drupal.org')) doProfile = it
}

def tableId = doProfile.getTableId().getValue();
URL dataUrl = new URL("https://www.google.com/analytics/feeds/data");
DataQuery query = new DataQuery(dataUrl);
query.setIds(tableId);
query.setStartDate("2009-09-29");
query.setEndDate("2009-09-29");
query.setDimensions("ga:eventCategory,ga:eventAction");
query.setMetrics("ga:uniqueEvents");
query.setFilters("ga:eventCategory==PivotsPageview_5002");
query.setSort("-ga:uniqueEvents");
query.setMaxResults(10000); // hard-limit is 10,000. double check in the future to prevent changes.
DataFeed data = myService.getFeed(query, DataFeed.class);

data.getEntries().each { entry ->
  category = entry.stringValueOf("ga:eventCategory");
  action = entry.stringValueOf("ga:eventAction");
  count = entry.longValueOf("ga:uniqueEvents");
  println "${category}, ${action}, ${count}";
}