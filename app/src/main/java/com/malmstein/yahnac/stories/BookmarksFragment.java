package com.malmstein.yahnac.stories;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.presenters.BookmarksAdapter;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.recyclerview.FeedRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

public class BookmarksFragment extends HNewsFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOKMARKS_LOADER = 0;

    private BookmarksAdapter bookmarksAdapter;
    private RecyclerView bookmarksList;
    private RecyclerView.LayoutManager bookmarksLayoutManager;
    private StoryListener listener;

    private SnackBarView snackbarView;
    private int croutonBackgroundAlpha;
    private long croutonAnimationDuration;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BOOKMARKS_LOADER, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (StoryListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks_feed, container, false);

        bookmarksList = Views.findById(rootView, R.id.list_bookmarks);
        snackbarView = Views.findById(rootView, R.id.feed_page_snackbar);

        this.croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        this.croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);

        setupStoriesList();

        return rootView;
    }

    private void setupStoriesList() {
        bookmarksList.setHasFixedSize(true);
        bookmarksLayoutManager = new LinearLayoutManager(getActivity());
        bookmarksList.addItemDecoration(createItemDecoration(getResources()));
        bookmarksList.setLayoutManager(bookmarksLayoutManager);

        bookmarksAdapter = new BookmarksAdapter(null, listener);
        bookmarksList.setAdapter(bookmarksAdapter);
    }

    private FeedRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new FeedRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    protected String getOrder() {
        return HNewsContract.BookmarkEntry.TIMESTAMP + " DESC";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri bookmarksUri = HNewsContract.BookmarkEntry.buildBookmarksUri();

        return new CursorLoader(
                getActivity(),
                bookmarksUri,
                HNewsContract.BookmarkEntry.BOOKMARK_COLUMNS,
                null,
                null,
                getOrder());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookmarksAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookmarksAdapter.swapCursor(null);
    }

}
