package nl.food4bees.beedroid;

import org.apache.http.client.CookieStore;

public class CookieStoreStore {
    static private final CookieStoreStore mCookieStoreStore = new CookieStoreStore();

    static private CookieStore mCookieStore = null;
 
    private CookieStoreStore() {
    }

    static public CookieStoreStore getInstance() {
        return mCookieStoreStore;
    }

    static public void setCookieStore(CookieStore cookieStore)
    {
        mCookieStore = cookieStore;
    }

    static public CookieStore getCookieStore()
    {
        return mCookieStore;
    }
}
