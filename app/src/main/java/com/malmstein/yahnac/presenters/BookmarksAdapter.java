package com.malmstein.yahnac.presenters;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.stories.StoryListener;
import com.novoda.notils.caster.Views;

public class BookmarksAdapter extends CursorRecyclerAdapter<BookmarksAdapter.ViewHolder> {

    private final StoryListener listener;

    public BookmarksAdapter(Cursor cursor, StoryListener listener) {
        super(cursor);
        this.listener = listener;
    }

    private Intent createShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        final Story story = Story.fromBookmark(cursor);

        holder.title.setText(story.getTitle());
        if (story.hasDomain()) {
            holder.domain.setText(story.getDomain());
        } else {
            holder.domain.setVisibility(View.GONE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContentClicked(story);
            }
        });

        holder.share_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked(createShareIntent(story.getUrl()));
            }
        });

        holder.bookmark_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookmarkClicked(story);
            }
        });

        if (story.isStoryAJob()) {
            holder.user.setVisibility(View.GONE);
            holder.comments_action.setVisibility(View.GONE);
        } else {
            holder.user.setText(holder.user.getResources().getString(R.string.story_by, story.getSubmitter()));
            holder.comments_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentsClicked(v, story);
                }
            });
        }

        if (story.isHackerNewsLocalItem()) {
            holder.external_action.setVisibility(View.GONE);
        } else {
            holder.external_action.setVisibility(View.VISIBLE);
            holder.external_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onExternalLinkClicked(story);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView user;
        public final View card;
        public final View external_action;
        public final View share_action;
        public final View comments_action;
        public final View bookmark_action;
        public final TextView domain;

        public ViewHolder(View view) {
            super(view);
            title = Views.findById(view, R.id.article_title);
            user = Views.findById(view, R.id.article_user);
            card = Views.findById(view, R.id.article_card_root);
            external_action = Views.findById(view, R.id.article_external_action);
            share_action = Views.findById(view, R.id.article_share_action);
            comments_action = Views.findById(view, R.id.article_comments_action);
            bookmark_action = Views.findById(view, R.id.article_bookmark_action);
            domain = Views.findById(view, R.id.article_domain);
        }
    }

}