package adapter;

/**
 * Created by Mahdi on 2/7/2016.
 */


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahdi.askhow.R;

import java.util.Arrays;
import java.util.List;

import Items.Post;
import libs.LetterAvatar;
import libs.MethodLibs;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>  {

    private List<Post> postList;
    private TypedArray mColors;
    Context context;
    public LinearLayout tags_ll;
    TextView tag;
    LinearLayout.LayoutParams lp;
    private static ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, desc, poster, date, votesDigit;
        public RelativeLayout postBG;
        public LinearLayout postContent;
        public ImageView avatar;
        public TextView[] tvs = new TextView[10];
        public int color;
        public int color2;
        public List<String> tags;
        public ImageButton showMore;

        public ImageView upVote, downVote;
        public ViewGroup parent;
        public boolean isChecked = false;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.questionTitleTV);
            desc = (TextView) view.findViewById(R.id.questionDescTV);
            poster = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.timestamp);
            avatar = (ImageView) view.findViewById(R.id.profilePic);
            postBG = (RelativeLayout) view.findViewById(R.id.post_bg);
            postContent = (LinearLayout) view.findViewById(R.id.postContent);
            votesDigit = (TextView) view.findViewById(R.id.voteDisplayDigit);
            tags_ll = (LinearLayout) view.findViewById(R.id.tags_ll);
            upVote = (ImageView) view.findViewById(R.id.upvote);
            downVote = (ImageView) view.findViewById(R.id.downvote);
            showMore = (ImageButton) view.findViewById(R.id.show_more);
            parent = (ViewGroup) view.findViewById(R.id.fabCL);

//            year = (TextView) view.findViewById(R.id.year);

            final Typeface questionTitleFont = Typeface.createFromAsset(view.getContext().getAssets(), "IRTitr.ttf");
            final Typeface questionDescFont = Typeface.createFromAsset(view.getContext().getAssets(), "IRTerafik.ttf");
            title.setTypeface(questionTitleFont);
            desc.setTypeface(questionDescFont);
            Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "Jersey M54.ttf");
            votesDigit.setTypeface(font);

//            poster.setTypeface(font);
//            date.setTypeface(font);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            int tags_margin = (int)view.getContext().getResources().getDimension(R.dimen.tags_margin);


            for (int i = 0; i < 10; i++) {
                tag = (TextView) inflater.inflate(R.layout.tag_layout, null, false);
                params.setMargins(tags_margin, tags_margin, tags_margin, tags_margin);
                tag.setLayoutParams(params);
//                tag.setText(tags.get(i));
                tag.setTypeface(questionDescFont);
                tags_ll.addView(tag);
                tvs[i] = tag;
            }


            postContent.setOnClickListener(this);


            showMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Drawable drawable = avatar.getDrawable();
                    if (drawable instanceof Animatable) {
                        poster.setText("Animated");
                        ((Animatable) drawable).start();
                    }

                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(context, v);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(context, "You Clicked : " + item.getTitle() + " " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });//closing the setOnClickListener method



//            drawable = downVote.getDrawable();
//            if (drawable instanceof Animatable) {
//                ((Animatable) drawable).start();
//            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

//        @Override
//        public boolean onLongClick(View v) {
//            clickListener.onItemLongClick(getAdapterPosition(), v);
//            return false;
//        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PostsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
//        void onItemLongClick(int position, View v);
    }


    public PostsAdapter(List<Post> postList) {
        this.postList = postList;
    }


//    private final DialogInterface.OnClickListener mOnClickListener = new MyOnClickListener();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testlayout2, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Post post = postList.get(position);
        context = holder.itemView.getContext();

        holder.title.setText(post.getPostTitle());
        holder.desc.setText(post.getPostDesc());
        holder.poster.setText(post.getPosterName() + " - " + post.getDate());
        holder.date.setText(post.getAnswers() + " answers - " + post.getViews() + " views");
        holder.votesDigit.setText(post.getVotes() + " ");

        holder.title.setTextColor(Color.WHITE);
        holder.desc.setTextColor(Color.WHITE);
        holder.poster.setTextColor(Color.WHITE);
        holder.date.setTextColor(Color.WHITE);


        holder.tags = Arrays.asList(post.getTags().split(","));


        final Resources res = context.getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        final LetterAvatar tileProvider = new LetterAvatar(context);
        Bitmap letterTile;// = tileProvider.getLetterTile(post.getPosterName(), post.getPosterName(), tileSize, tileSize);

        if(Math.abs(post.getPost_mysql_id()) % 8 == Math.abs(post.getPosterName().hashCode()) % 8)
        {
            letterTile = tileProvider.getLetterTile(post.getPosterName(), post.getPosterName() + post.getPost_mysql_id(), tileSize, tileSize);
        }
        else
            letterTile = tileProvider.getLetterTile(post.getPosterName(), post.getPosterName(), tileSize, tileSize);


        holder.avatar.setImageBitmap(MethodLibs.getCircularBitmap(letterTile));


        mColors = context.getResources().obtainTypedArray(R.array.background_colors);
        TypedArray tColors = context.getResources().obtainTypedArray(R.array.background_colors);

//        final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
//        final Canvas c = new Canvas();
//        c.setBitmap(bitmap);

        holder.color = Math.abs(post.getPost_mysql_id()) % 8;
        holder.postBG.setBackgroundColor(tColors.getColor(holder.color, Color.BLACK));//pickColor(String.valueOf(post.getPost_mysql_id())));


        for (int i = 0; i < 10; i++)
        {
            if(i >= holder.tags.size())
                holder.tvs[i].setVisibility(View.GONE);
            else{
                holder.tvs[i].setVisibility(View.VISIBLE);
                holder.tvs[i].setText(holder.tags.get(i));
//                holder.color2 = (Math.abs(holder.tags.get(i).hashCode())) % 8;
                if(Math.abs(post.getPost_mysql_id()) % 17 == (Math.abs(holder.tags.get(i).hashCode())) % 8)
                {
                    holder.color = (Math.abs(holder.tags.get(i).hashCode()) + 1) % 8;
                }
                else
                    holder.color = Math.abs(holder.tags.get(i).hashCode()) % 8;

                holder.tvs[i].setBackgroundColor(mColors.getColor(holder.color, Color.BLACK));
            }
        }


        if(!post.isChecked())
        {
//            holder.upVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_upvote_to_checkmark));
            holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_upvote_to_checkmark));
        }
        else
        {
//            holder.upVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_checkmark_to_upvote));
            holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_checkmark_to_upvote));
        }



        holder.upVote.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View v) {

                if(!post.isChecked()){
                    post.setIsChecked(true);
                    holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_upvote_to_checkmark));

                }

                else
                {
                    post.setIsChecked(false);
                    holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_checkmark_to_upvote));

                }


                Drawable drawable;
                drawable = holder.upVote.getDrawable();

                if (drawable instanceof Animatable) {
                    if(!(((Animatable) drawable).isRunning()))
                        ((Animatable) drawable).start();
                }
            }
        });


//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.main, null);



//        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
//
//        final LetterTileProvider tileProvider = new LetterTileProvider(holder.itemView.getContext());
//        final Bitmap letterTile = tileProvider.getLetterTile(post.getPostTitle(), post.getPostDesc(), 20, 20);
//        holder.postBG.setBackground(new BitmapDrawable(res, letterTile));
//        holder.avatar.setImageBitmap(letterTile);

//        getActionBar().setIcon(new BitmapDrawable(getResources(), letterTile));

//        holder.year.setText(post.getYear());

//        swapView(createMorphableView(R.drawable.animated_vector_upvote, R.drawable.animated_vector_checkmarkup, Color.parseColor("#ffffff"),holder.itemView.getContext()), holder.parent, R.id.upvote);
//        swapView(createMorphableView(R.drawable.animated_vector_downvote, R.drawable.animated_vector_checkmarkdown, Color.parseColor("#ffffff"),holder.itemView.getContext()), holder.parent, R.id.downvote);

    }

    private int pickColor(String key) {
        // String.hashCode() is not supposed to change across java versions, so
        // this should guarantee the same key always maps to the same color
        final int color = Math.abs(key.hashCode()) % 8;
        try {
            return mColors.getColor(color, Color.BLACK);
        } finally {
            mColors.recycle();
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Post getItem(int position) {
        return postList.get(position);
    }

//    private void swapView(View newView, final ViewGroup vg, int id){
//        View toRemove = vg.findViewById(id);
//        vg.removeView(toRemove);
//        newView.setId(id);
//        vg.addView(newView, toRemove.getLayoutParams());
//        ((MorphButton)newView).setState(MorphButton.MorphState.START, true);
//        ((MorphButton)newView).setOnStateChangedListener(new MorphButton.OnStateChangedListener() {
//            @Override
//            public void onStateChanged(MorphButton.MorphState changedTo, boolean isAnimating) {
//                if(changedTo== MorphButton.MorphState.START)
//                    (vg.findViewById(R.id.downvote)).setVisibility(View.VISIBLE);
//                else
//                    (vg.findViewById(R.id.downvote)).setVisibility(View.GONE);
//            }
//        });
//
////        View toRemove = vg.findViewById(R.id.upvote);
////        vg.removeView(toRemove);
////        newView.setId(R.id.upvote);
////        vg.addView(newView, toRemove.getLayoutParams());
//    }
//
//    private View createMorphableView(int startDrawable, int endDrawable, int color, Context context){
//        MorphButton mb = new MorphButton(context);
//        mb.setForegroundTintList(ColorStateList.valueOf(color));
//        mb.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);
//        mb.setBackgroundColor(Color.TRANSPARENT);
//        mb.setStartDrawable(startDrawable);
//        mb.setEndDrawable(endDrawable);
//        mb.setState(MorphButton.MorphState.END,true);
//        return  mb;
//    }
}

