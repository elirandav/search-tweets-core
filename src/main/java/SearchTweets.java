/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class SearchTweets {
    /**
     * Usage: java twitter4j.examples.search.SearchTweets [query]
     *
     * @param args search query
     */

    private Twitter twitter;

    public SearchTweets() {
        Properties properties = initProperties();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(properties.getProperty("OAuthConsumerKey"))
                .setOAuthConsumerSecret(properties.getProperty("OAuthConsumerSecret"))
                .setOAuthAccessToken(properties.getProperty("OAuthAccessToken"))
                .setOAuthAccessTokenSecret(properties.getProperty("OAuthAccessTokenSecret"));
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    private Properties initProperties() {
        Properties catalogProps = null;
        try {
            String catalogConfigPath = "C:\\twitter\\twitter.config";
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(catalogConfigPath));
            catalogProps = new Properties();
            catalogProps.load(new FileInputStream(catalogConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return catalogProps;
    }

    public void getTop5(String textToSearch) {
        try {
            System.out.println("Start search: " + textToSearch);
            Query query = new Query(textToSearch);
            int maxItem = 5;
            runSearch(query, maxItem);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        System.out.println("Done search: " + textToSearch);
    }

    private void runSearch(Query query, int maxItems) throws TwitterException {
        QueryResult result;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                if (maxItems-- == 0) {
                    return;
                }
            }
        } while ((query = result.nextQuery()) != null);
    }
}