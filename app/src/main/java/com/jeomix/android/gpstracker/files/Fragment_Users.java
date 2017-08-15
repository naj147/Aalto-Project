package com.jeomix.android.gpstracker.files;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.Helper.UserHelper;

/**
 * Created by jeomix on 8/12/17.
 */


public class Fragment_Users extends Fragment {
    private RecyclerView recyclerView;
    private MyRecycle_Adapter adapter=null;
    private ImageView emptyView;
    private View view;
    private Paint p = new Paint();
    FirebaseDatabase database;
    DatabaseReference myUsersRef;
    ValueEventListener vel;
    boolean isUser=true;
    DatabaseReference myVehRef;
    private AlertDialog.Builder alertDialog;
    private EditText et_country;
    private int edit_position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usersandcars, container, false);
        emptyView= (ImageView) view.findViewById(R.id.emptyCardsImageView);
        Glide.with(getContext()).load(R.drawable.ic_empty_set).placeholder(R.drawable.ic_empty_set).into(emptyView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        recyclerView = (RecyclerView)view.findViewById(R.id.historyRecycleView);
        database= FirebaseDatabase.getInstance();
        setupRecycleView(recyclerView,getContext());
        return view;
    }
    public void setupRecycleView(RecyclerView recyclerView, Context context){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter= new MyRecycle_Adapter();
        recyclerView.setAdapter(adapter);
        if(!isUser){
            adapter.switch_loading();
            refreshVehicles();
        }else
            refreshUsers();
        initSwipe();
        isImageEmpty();
    }
    public void switch_loading(){
        isUser=!isUser;
    }

    public void refreshVehicles(){
        myVehRef = database.getReference("vehicules");
        DatabaseReference myUsersRef= database.getReference("users");
        vel=myVehRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //TODO Calculate Number of vehicules for Navigationdrawer
                long navigation_drawer_update  =dataSnapshot.getChildrenCount();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Vehicle v =data.getValue(Vehicle.class);
                    myUsersRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            assert v != null;
                            User user =dataSnapshot.child(v.getId()).getValue(User.class);
                            if(user!=null && user.getIsAdmin()==0)
                            adapter.addUserArray(new Users_Array(user,v));
                            isImageEmpty();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myVehRef.addValueEventListener(vel);
    }

    public void refreshUsers(){
        myUsersRef= database.getReference("users");
        DatabaseReference myVehRef = database.getReference("vehicules");
//        ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setIndeterminate(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Loading Data From DB");
//        progressDialog.setMessage("Please Wait Loading Content...");
//        progressDialog.show();
         vel= myUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user=data.getValue(User.class);
                    myVehRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users_Array user_array = new Users_Array(user);
                            if(user!=null && dataSnapshot.child(user.getId()).exists()){
                                Vehicle v= dataSnapshot.child(user.getId()).getValue(Vehicle.class);
                                user_array.setVehicle(v);
                            }
                            adapter.addUserArray(user_array);
                            isImageEmpty();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        myUsersRef.addValueEventListener(vel);
    }

    public void isImageEmpty(){
        if(recyclerView.getAdapter().getItemCount()>0)
            emptyView.setVisibility(View.INVISIBLE);
        else
            emptyView.setVisibility(View.VISIBLE);
    }

//    public void eventStarter(int position){
//        History_Array n=adapter.getItem(position);
//        if(n!=null)
//            EventBus.getDefault().postSticky(n);
//    }

    /*
       * A function Used to retrieve the Action appropiate to the card user role
       * */
    public void swip_action(Users_Array users_array,boolean isLeft){
        User user= users_array.getUser();
        Vehicle v= users_array.getVehicle();
        if(isUser){
            if(user!=null){
                if(isLeft){
                    switch(user.getIsAdmin()){
                        case 0:
                            adapter.ban(user);
                            break;
                        case 2:
                            adapter.ban(user);
                            break;
                        case 1:
                            adapter.ban(user);
                            break;
                        case 3:
                            adapter.unBan(user,v);
                            break;
                    }
                }else {
                    switch (user.getIsAdmin()) {
                        case 0:
                            adapter.track(v,true);
                            break;
                        case 2:
                            adapter.ban(user);
                            break;
                        case 1:
                            adapter.acceptAdmin(user);
                            break;
                        case 3:
                            adapter.unBan(user, v);
                            break;
                    }
                }
        }}
        else{
                if(isLeft){
                    adapter.track(v,false);
                }else
                    adapter.track(v,true);
            }
    }

    /*
    * A function Used to retrieve the Image appropiate to the perpetuated action
    * */
    public Bitmap getSwipeImage(Users_Array users_array,Boolean isLeft) {
        User user= users_array.getUser();
        int imgRes=R.drawable.bubble;
        if (isLeft) {
            switch (user.getIsAdmin()) {
                case 0:
                    if(isUser)
                        imgRes=R.drawable.ban;
                    else
                        imgRes=R.drawable.untrack;
                    break;
                    case 2 :case 1:
                    imgRes=R.drawable.ban;
                    break;
                case 3:
                   imgRes=R.drawable.unchecked;
                    break;
            }
        } else {
            switch (user.getIsAdmin()) {
                case 0:
                    imgRes=R.drawable.track;
                    break;
                case 2:
                    imgRes=R.drawable.ban;
                    break;
                case 1:
                    imgRes=R.drawable.accept_pending;
                    break;
                case 3:
                    imgRes=R.drawable.unchecked;
                    break;
            }
        }
       return getBitmapFromDrawable(getContext(), imgRes);
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    Log.v("Item"+position+" :", "SWIPED LEFT");
                    swip_action(adapter.getUserArray(position),true);
                    isImageEmpty();
                } else {
                    swip_action(adapter.getUserArray(position),false);
                    Log.v("Item "+position+" :", "SWIPED RIGHT");
                }
            }

            private void removeView(){
                if(view.getParent()!=null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Users_Array users_array = adapter.getUserArray(viewHolder.getAdapterPosition());
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE & isCurrentlyActive){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){


                        icon = getSwipeImage(users_array,false);
                        p.setColor(Color.parseColor("#FFF9C4"));

                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        // icon.isRecycled();
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    } else {
                        p.setColor(Color.parseColor("#FFFFFF"));
                        icon = getSwipeImage(users_array,true);

                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        icon.isRecycled();
                        c.drawBitmap(icon,null,icon_dest,p);
                        /* Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
                             Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                         drawable.draw(canvas);*/
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        if(myUsersRef!=null){
            if(vel!=null)
            myUsersRef.removeEventListener(vel);
        }
        if(myVehRef!=null){
            if(vel!=null)
                myVehRef.removeEventListener(vel);
        }
        super.onStop();
    }


//    private void initDialog(){
//        alertDialog = new AlertDialog.Builder(this);
//        view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
//        alertDialog.setView(view);
//        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(add){
//                    add =false;
//                    adapter.addItem(et_country.getText().toString());
//                    dialog.dismiss();
//                } else {
//                    countries.set(edit_position,et_country.getText().toString());
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//
//            }
//        });
//        et_country = (EditText)view.findViewById(R.id.et_country);
//    }
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.fab:
//                removeView();
//                add = true;
//                alertDialog.setTitle("Add Country");
//                et_country.setText("");
//                alertDialog.show();
//                break;
//        }
//    }
    /*
        private  MaterialListView mListView;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.history_card, container, false);
            mListView = (MaterialListView) view.findViewById(R.id.material_listview);
            mListView.setItemAnimator(new SlideInLeftAnimator());
            mListView.getItemAnimator().setAddDuration(300);
            mListView.getItemAnimator().setRemoveDuration(300);
            final ImageView emptyView = (ImageView) view.findViewById(R.id.imageView);
            emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mListView.setEmptyView(emptyView);
            Glide.with(this)
                    .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                    .thumbnail(1)
                    .into(emptyView);

            // Fill the array withProvider mock content
            fillArray();

            // Set the dismiss listener
            mListView.setOnDismissCallback(new OnDismissCallback() {
                @Override
                public void onDismiss(@NonNull Card card, int position) {
                    // Show a toast
                    Toast.makeText(getContext(), "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the ItemTouchListener
            mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull Card card, int position) {
                    Log.d("CARD_TYPE", "" + card.getTag());
                }

                @Override
                public void onItemLongClick(@NonNull Card card, int position) {
                    Log.d("LONG_CLICK", "" + card.getTag());
                }
            });

            return view;

        }
        private void fillArray() {
            List<Card> cards = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                cards.add(historyCard());
            }
            mListView.getAdapter().addAll(cards);
        }

        /*

        public Card historyCard() {
    //        Card card = new Card.Builder(getContext())
    //                .withProvider(new CardProvider())
    //                .setLayout(R.layout.material_basic_image_buttons_card_layout)
    //                .setTitle("Card number 3")
    //                .setTitleGravity(Gravity.END)
    //                .setDescription("Lorem ipsum dolor sit amet")
    //                .setDescriptionGravity(Gravity.END)
    //                .setDrawable(R.drawable.pic_5)/*Glide.
    //                        with(this).
    //                        load("http://....").
    //                        asBitmap().
    //                        into(100, 100).
    //                        get()*/
//                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
//                    @Override
//                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
//                        requestCreator.fit();
//                    }
//                })
//                .addAction(R.id.left_text_button, new TextViewAction(this.getContext())
//                        .setText("Izquierda")
//                        .setTextResourceColor(R.color.black_button)
//                        .setListener(new OnActionClickListener() {
//                            @Override
//                            public void onActionClicked(View view, Card card) {
//                                Toast.makeText(getContext(), "You have pressed the left button", Toast.LENGTH_SHORT).show();
//                                card.getProvider().setTitle("CHANGED ON RUNTIME");
//                            }
//                        }))
//                .addAction(R.id.right_text_button, new TextViewAction(getContext())
//                        .setText("Derecha")
//                        .setTextResourceColor(R.color.orange_button)
//                        .setListener(new OnActionClickListener() {
//                            @Override
//                            public void onActionClicked(View view, Card card) {
//                                Toast.makeText(getContext(), "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
//                //                cardList.remove(card);
//                                card.dismiss();
//                            }
//                        }))
//                .endConfig()
//                .build();
    /*
        final CardProvider provider = new Card.Builder(this.getContext())
                .setTag("BASIC_IMAGE_BUTTON_CARD")
                .setDismissible()
                .withProvider(new CardProvider<>())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("shit")
                .setTitleGravity(Gravity.END)
                .setDescription("Nada")
                .setDescriptionGravity(Gravity.END)
                .setDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.pic_5),100,100,false)))
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit();
                    }
                })
                .addAction(R.id.left_text_button, new TextViewAction(this.getContext())
                        .setText("left")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getContext(), "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                card.getProvider().setTitle("CHANGED ON RUNTIME");
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this.getContext())
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getContext(), "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                card.dismiss();
                            }
                        }));

            provider.setDividerVisible(true);
        return provider.endConfig().build();
    }*/
//    public static Bitmap getCircularBitmap(Bitmap bitmap) {
//        Bitmap output;
//
////        if (bitmap.getWidth() > bitmap.getHeight()) {
////            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
////        } else {
////            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
////        }
//      output = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, 100, 100);
//
//        float r = 50;
//
////        if (bitmap.getWidth() > bitmap.getHeight()) {
////            r = bitmap.getHeight() / 2;
////        } else {
////            r = bitmap.getWidth() / 2;
////        }
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawCircle(r, r, r, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
//    }
}
