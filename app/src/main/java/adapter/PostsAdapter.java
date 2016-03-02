package adapter;

/**
 * Created by Mahdi on 2/7/2016.
 */


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mahdi.askhow.R;
import com.wnafee.vector.MorphButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Items.Post;
import libs.AppController;
import libs.LetterAvatar;
import libs.MethodLibs;
import libs.SessionManager;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private List<Post> postList;
    private TypedArray mColors;
    Context context;
    public LinearLayout tags_ll;
    TextView tag;
    LinearLayout.LayoutParams lp;
    private static ClickListener clickListener;
    private String urlJsonArry = "http://192.168.42.190/askhow/v1/vote/:";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        public boolean voteDown_isChecked;

        public Drawable drawable;
        AnimatorSet set;

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
            voteDown_isChecked = false;

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
            int tags_margin = (int) view.getContext().getResources().getDimension(R.dimen.tags_margin);


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


//    private final DialogInterface.OnClickListener mOnClickListener = new MyCustomCallBack();

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

        if (Math.abs(post.getPost_mysql_id()) % 8 == Math.abs(post.getPosterName().hashCode()) % 8) {
            letterTile = tileProvider.getLetterTile(post.getPosterName(), post.getPosterName() + post.getPost_mysql_id(), tileSize, tileSize);
        } else
            letterTile = tileProvider.getLetterTile(post.getPosterName(), post.getPosterName(), tileSize, tileSize);


        holder.avatar.setImageBitmap(MethodLibs.getCircularBitmap(letterTile));


        mColors = context.getResources().obtainTypedArray(R.array.background_colors);
        TypedArray tColors = context.getResources().obtainTypedArray(R.array.background_colors);

//        final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
//        final Canvas c = new Canvas();
//        c.setBitmap(bitmap);

        holder.color = Math.abs(post.getPost_mysql_id()) % 8;
        holder.postBG.setBackgroundColor(tColors.getColor(holder.color, Color.BLACK));//pickColor(String.valueOf(post.getPost_mysql_id())));


        for (int i = 0; i < 10; i++) {
            if (i >= holder.tags.size())
                holder.tvs[i].setVisibility(View.GONE);
            else {
                holder.tvs[i].setVisibility(View.VISIBLE);
                holder.tvs[i].setText(holder.tags.get(i));
//                holder.color2 = (Math.abs(holder.tags.get(i).hashCode())) % 8;
                if (Math.abs(post.getPost_mysql_id()) % 17 == (Math.abs(holder.tags.get(i).hashCode())) % 8) {
                    holder.color = (Math.abs(holder.tags.get(i).hashCode()) + 1) % 8;
                } else
                    holder.color = Math.abs(holder.tags.get(i).hashCode()) % 8;

                holder.tvs[i].setBackgroundColor(mColors.getColor(holder.color, Color.BLACK));
            }
        }


//        if (post.isUpVoteChecked()) {
//            holder.upVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_checkmark_to_upvote));
////            holder.downVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_downvote_to_checkmark));
//        } else {
//            holder.upVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_upvote_to_loading));
////            holder.downVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_checkmark_to_downvote));
//        }

//        if(post.isDownVoteChecked())
//        {
//            holder.downVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_checkmark_to_downvote));
//        }
//        else{
//            holder.downVote.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.animated_downvote_to_checkmark));
//        }

//
//        holder.downVote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (!post.isDownVoteChecked()) {
//                    holder.downVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_downvote_to_checkmark));
//                    post.setIsDownVoteChecked(true);
//
//                } else {
//                    holder.downVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_checkmark_to_downvote));
//                    post.setIsDownVoteChecked(false);
//                }
//
//
//                Drawable drawable;
//                drawable = holder.downVote.getDrawable();
//
//                if (drawable instanceof Animatable) {
//                    if (!(((Animatable) drawable).isRunning()))
//                        ((Animatable) drawable).start();
//                }
//            }
//        });

//        final AnimatedVectorDrawableCompat d = (AnimatedVectorDrawableCompat) context.getDrawable(R.drawable.animated_upvote_to_checkmark); // Insert your AnimatedVectorDrawable resource identifier
//        holder.upVote.setImageDrawable(d);
//        d.registerAnimationCallback(callback);
//
        holder.upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNetworkCall(post.getPost_mysql_id(),1, holder);
//                if (!d.isRunning())
//                    d.start();
//                listener(post, holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Post getItem(int position) {
        return postList.get(position);
    }

//    public void listener(Post post, MyViewHolder holder)
//    {
//
//
//        if(!post.isUpVoteChecked()){
//            post.setIsUpVoteChecked(true);
//            holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_upvote_to_checkmark));
//            makeNetworkCall(post.getPost_mysql_id(), 1, holder);
//        }
//
//        else
//        {
//            post.setIsUpVoteChecked(false);
//            holder.upVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_checkmark_to_upvote));
//            makeNetworkCall(post.getPost_mysql_id(), 0, holder);
//        }
//
//
//        if(post.isDownVoteChecked())
//        {
//            holder.downVote.setImageDrawable(context.getResources().getDrawable(R.drawable.animated_checkmark_to_downvote));
//            post.setIsDownVoteChecked(false);
//            Drawable drawable1 = holder.downVote.getDrawable();
//            if (drawable1 instanceof Animatable) {
//                if(!(((Animatable) drawable1).isRunning()))
//                    ((Animatable) drawable1).start();
//            }
//        }
//
//        holder.drawable = holder.upVote.getDrawable();
//
//        if (holder.drawable instanceof Animatable) {
//            if(!(((Animatable) holder.drawable).isRunning()))
//                ((Animatable) holder.drawable).start();
//        }
//    }

    public void makeNetworkCall(final int postID, final int vote_type, final ImageView view) {
        final SessionManager sm = new SessionManager(context);
        StringRequest sr = new StringRequest(Request.Method.PUT, urlJsonArry + postID , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ok", response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    String Error = json.getString("error");
                    if(Error.equals("false"))
                    {
                        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.GREEN,
                                PorterDuff.Mode.SRC_ATOP);
                        view.setColorFilter(porterDuffColorFilter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error.respsonse", "Error: " + error.getMessage());
                Log.d("error", ""+error.getMessage()+","+error.toString());
            }
        }){



            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<> ();
                params.put("post_id", String.valueOf(postID));
                params.put("vote_type", String.valueOf(vote_type));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Accept", "application/json");
                headers.put("Authorization", sm.getToken());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }
}

