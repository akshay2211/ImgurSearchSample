package com.interview.project.ui.singlepost;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.MaterialToolbar;
import com.interview.project.R;
import com.interview.project.data.local.AppDatabase;
import com.interview.project.model.Comments;
import com.interview.project.model.Images;
import com.interview.project.ui.adapters.CommentsAdapter;
import com.interview.project.ui.utils.Constants;
import com.interview.project.ui.utils.CustomWindow;
import com.interview.project.ui.utils.GlideApp;
import com.interview.project.ui.utils.GlideRequests;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Lazy;
import kotlin.coroutines.CoroutineContext;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static org.koin.java.KoinJavaComponent.inject;

public class SingleActivity extends AppCompatActivity {
    private static final String IMAGES_TAG = "images_tag";
    private final Lazy<AppDatabase> db = inject(AppDatabase.class);
    private final Lazy<CoroutineContext> coroutineContextLazy = inject(CoroutineContext.class);
    CommentsAdapter commentsAdapter = new CommentsAdapter();

    Images images = null;

    public static void startActivity(@Nullable Activity context, @NotNull Images images) {
        Intent i = new Intent(context, SingleActivity.class);
        i.putExtra(IMAGES_TAG, images);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(i, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            context.startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomWindow.setUpStatusNavigationBarColors(getWindow(), false, Color.WHITE);
        setContentView(R.layout.activity_single);
        initialise();
        observe();
    }

    private void observe() {
        db.getValue().commentsDao().getAllComments(images.getId()).observe(this, comments -> {
            Log.e("retrieveing ", "comments " + comments.size());
            commentsAdapter.addDataInList(comments);
        });
    }

    private void initialise() {
        GlideRequests glideRequests = GlideApp.with(this);
        Intent intent = getIntent();
        images = (Images) intent.getSerializableExtra(IMAGES_TAG);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        ImageView imageView = findViewById(R.id.singleImageView);
        AppCompatEditText appCompatEditText = findViewById(R.id.appCompatEditText);
        AppCompatImageView appCompatImageView = findViewById(R.id.appCompatImageView);
        RecyclerView commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setAdapter(commentsAdapter);
        toolbar.setNavigationOnClickListener(v -> finish());
        imageView.post(() -> {
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.width = Constants.INSTANCE.getWidthPX();
            lp.height = (images.getHeight() * lp.width) / images.getWidth();
            imageView.setLayoutParams(lp);
            imageView.requestLayout();
        });

        glideRequests.load(images.getLink())
                .placeholder(new ColorDrawable(Color.CYAN))
                .error(new ColorDrawable(Color.GRAY))
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        appCompatImageView.setOnClickListener(v -> {
            String comment_text = appCompatEditText.getText().toString();
            if (!comment_text.trim().isEmpty()) {
                new Thread(() -> db.getValue().runInTransaction(() -> {
                    Comments c = new Comments(images.getId(), comment_text);
                    db.getValue().commentsDao().insert(c);
                })).start();
                appCompatEditText.setText("");
            }
        });
    }

}