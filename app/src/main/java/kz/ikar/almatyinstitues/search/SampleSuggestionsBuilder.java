package kz.ikar.almatyinstitues.search;

import android.content.Context;

import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kz.ikar.almatyinstitues.classes.Institute;

/**
 * Created by User on 23.05.2017.
 */

public class SampleSuggestionsBuilder implements SearchSuggestionsBuilder {
    private List<Institute> mInstitutes = new ArrayList<>();
    private Context mContext;
    private List<SearchItem> mHistorySuggestions = new ArrayList<SearchItem>();;

    public SampleSuggestionsBuilder(Context context, List<Institute> institutes) {
        this.mContext = context;
        this.mInstitutes = institutes;
        createHistorys();
    }

    private void createHistorys() {
        /*for (Institute inst : mInstitutes) {
            SearchItem item = new SearchItem(
                    inst.getName(),
                    inst.getAddress(),
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY);
            mHistorySuggestions.add(item);
        }*/
        /*SearchItem item1 = new SearchItem(
                "Isaac Newton",
                "Isaac Newton",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item1);
        SearchItem item2 = new SearchItem(
                "Albert Einstein",
                "Albert Einstein",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item2);
        SearchItem item3 = new SearchItem(
                "John von Neumann",
                "John von Neumann",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item3);
        SearchItem item4 = new SearchItem(
                "Alan Mathison Turing",
                "Alan Mathison Turing",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item4);*/
    }

    @Override
    public Collection<SearchItem> buildEmptySearchSuggestion(int maxCount) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        items.addAll(mHistorySuggestions);
        return items;
    }

    @Override
    public Collection<SearchItem> buildSearchSuggestion(int maxCount, String query) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        if (!query.trim().equals("") || !query.isEmpty()) {
            for (Institute item : mInstitutes) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    items.add(new SearchItem(
                            item.getName(),
                            item.getAddress(),
                            SearchItem.TYPE_SEARCH_ITEM_SUGGESTION));
                }
            }
        }
        /*for(SearchItem item : mHistorySuggestions) {
            if(item.getValue().startsWith(query)) {
                items.add(item);
            }
        }*/
        return items;
    }
}
