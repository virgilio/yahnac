package com.malmstein.yahnac.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.login.InputFieldValidator;
import com.novoda.notils.caster.Views;

public class ReplyView extends FrameLayout {

    private InputFieldValidator inputFieldValidator = new InputFieldValidator();

    private View cancel;
    private View reply;
    private EditText comment;

    private Listener listener;

    public ReplyView(Context context) {
        super(context);
    }

    public ReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_reply_view, this, true);
        cancel = Views.findById(this, R.id.reply_cancel);
        reply = Views.findById(this, R.id.reply_send);
        comment = Views.findById(this, R.id.reply_comment);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReplyCancelled();
                }
            }
        });

        reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (validate()) {
                        listener.onReplySent(comment.getText().toString());
                    }
                }
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private boolean validate() {
        String commentText = comment.getText().toString();
        boolean isCommentValid = inputFieldValidator.isValid(commentText);
        return isCommentValid;
    }

    public interface Listener {
        void onReplyCancelled();

        void onReplySent(String text);

    }
}
