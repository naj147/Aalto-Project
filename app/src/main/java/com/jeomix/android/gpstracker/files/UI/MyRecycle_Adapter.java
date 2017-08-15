package com.jeomix.android.gpstracker.files.UI;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.EventBusClasses.LocationEvents;
import com.jeomix.android.gpstracker.files.Objects.User;
import com.jeomix.android.gpstracker.files.Objects.Users_Array;
import com.jeomix.android.gpstracker.files.Objects.Vehicle;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jeomix on 8/12/17.
 */


public class MyRecycle_Adapter extends RecyclerView.Adapter<MyRecycle_Adapter.MyViewHolder> {
    private  ArrayList<Users_Array> data=null;
    private boolean isUsers=true;

    MyRecycle_Adapter(Users_Array entry) {
        data=new ArrayList<>();
        data.add(entry);
    }
    MyRecycle_Adapter(){
        data=new ArrayList<>();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bmb.clearBuilders();
        Users_Array currentUser=data.get(position);
        User user = currentUser.getUser();
        Vehicle v = currentUser.getVehicle();
        if(isUsers){
            switch( user.getIsAdmin()){
                case 0:
                    if(v!=null){
                        int imgRes=vehicleImage(v);
                        Glide.with(holder.itemView.getContext()).load(imgRes).placeholder(imgRes).into(holder.profilImageView);
                        holder.labelTextView.setText(v.getLabel());
                        holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.material_blue_500));
                        holder.descriptionTextView.setText(v.getVin());
                        holder.bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                        holder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
                        HamButton.Builder builder1 = new HamButton.Builder()
                                .normalImageRes(R.drawable.track)
                                .normalText("Track")
                                .subNormalText("Track this vehicle in real time") .rippleEffect(true).listener(i -> {
                                    //TODO Add The tracking functionnality
                                    track(v,true);
                                });
                        HamButton.Builder builder2 = new HamButton.Builder()
                                .normalImageRes(R.drawable.ban)
                                .normalText("Ban")
                                .subNormalText("Suspend the user's access to the app") .rippleEffect(true).listener(i -> {
                                    ban(user);
                                })
                                ;
                        holder.bmb.addBuilder(builder1);
                        holder.bmb.addBuilder(builder2);
                        break;
                    }
                case 1:
                    Glide.with(holder.itemView.getContext()).load(R.drawable.unchecked).placeholder(R.drawable.unchecked).into(holder.profilImageView);
                    String PENDING_ADMIN = "Admin Pending Approval";
                    holder.labelTextView.setText(PENDING_ADMIN);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.cold));
                    holder.descriptionTextView.setText(user.getEmail());
                    holder.bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                    holder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
                    HamButton.Builder builder4 = new HamButton.Builder()
                            .normalImageRes(R.drawable.ban)
                            .normalText("Ban")
                            .subNormalText("Suspend the user's access to the app") .rippleEffect(true).listener(i -> {
                                ban(user);
                            })
                            ;
                    HamButton.Builder builder5 = new HamButton.Builder()
                            .normalImageRes(R.drawable.accept_pending)
                            .normalText("Accept Admin")
                            .subNormalText("Accept Admin's admission") .rippleEffect(true).listener(i -> {
                                acceptAdmin(user);
                            })
                            ;
                    holder.bmb.addBuilder(builder4);
                    holder.bmb.addBuilder(builder5);
                    break;
                case 2 :
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(user.getEmail().charAt(0)), color);
                    holder.profilImageView.setImageDrawable(drawable);
                    String ADMIN = "Admin";
                    holder.labelTextView.setText(ADMIN);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.firey));
                    holder.descriptionTextView.setText(user.getEmail());
                    holder.bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_1);
                    holder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
                    HamButton.Builder builder3 = new HamButton.Builder()
                            .normalImageRes(R.drawable.ban)
                            .normalText("Ban")
                            .subNormalText("Suspend the user's access to the app") .rippleEffect(true).listener(i -> {
                                ban(user);
                            })
                            ;
                    holder.bmb.addBuilder(builder3);
                    break;

                case 3:
                    Glide.with(holder.itemView.getContext()).load(R.drawable.attention).placeholder(R.drawable.attention).into(holder.profilImageView);
                    String BANNED = "Banned";
                    holder.labelTextView.setText(BANNED);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.red));
                    holder.descriptionTextView.setText(user.getEmail());
                    holder.bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_1);
                    holder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
                    HamButton.Builder builder6 = new HamButton.Builder()
                            .normalImageRes(R.drawable.unchecked)
                            .normalText("UnBan")
                            .subNormalText("UnSuspend the user's access to the app") .rippleEffect(true).listener(i -> {
                                unBan(user,v);
                            })
                            ;
                    holder.bmb.addBuilder(builder6);
                    break;
            }
        }else{
            if(user.getIsAdmin()==0){
                int imgRes=vehicleImage(v);
                Glide.with(holder.itemView.getContext()).load(imgRes).placeholder(imgRes).into(holder.profilImageView);
                holder.labelTextView.setText(v.getLabel());
                holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.material_blue_500));
                holder.descriptionTextView.setText(v.getVin());
                holder.bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                holder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
                HamButton.Builder builder1= new HamButton.Builder()
                        .normalImageRes(R.drawable.track)
                        .normalText("Track")
                        .subNormalText("Track This vehicle") .rippleEffect(true).listener(i -> {
                            //TODO Add The tracking functionnality
                            track(v,true);
                        });
                HamButton.Builder builder2= new HamButton.Builder()
                            .normalImageRes(R.drawable.untrack)
                            .normalText("UnTrack")
                            .subNormalText("Untrack This vehicle") .rippleEffect(true).listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int i) {
                                //TODO Add The untracking functionnality
                                track(v,false);
                            }
                        });
                holder.bmb.addBuilder(builder1);
                holder.bmb.addBuilder(builder2);
            }
        }
    }

    public void track(Vehicle v,boolean istrack){
        EventBus.getDefault().post(new LocationEvents(v,istrack));
        notifyDataSetChanged();
    }
    public void ban(User user){
        int BAN = 3;
        changeIsAdmin(user, BAN);
    }
    public void acceptAdmin(User user){
        int ACCEPT = 2;
        changeIsAdmin(user, ACCEPT);
    }

    private void changeIsAdmin(User user, int isAdmin) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(user.getId()).child("isAdmin").setValue(isAdmin).addOnCompleteListener(task -> {
                user.setIsAdmin(isAdmin);
                updateUser(user);
        });
    }

    public void unBan(User user, Vehicle vehicle){
        if(vehicle==null){
            int PENDING_ACCEPT = 1;
            changeIsAdmin(user, PENDING_ACCEPT);
        }else
        {
            int VEHICLE = 0;
            changeIsAdmin(user, VEHICLE);
        }
    }

    private int vehicleImage(Vehicle v){
        if(v!=null){
            switch(v.getType()){
                case truck :
                   return R.drawable.if_truck;
                case motorCycle:

                    return  R.drawable.if_motorcycle;

                case car:
                    return R.drawable.if_car;

                case bus:
                    return R.drawable.if_schooolbus;

            }
        }
        return R.drawable.attention;
    }
    public void updateUser(User user){

        int i=getUserIndex(user);
        if(i!=-1){
            Users_Array usersArray= getUserArray(i);
            usersArray.setUser(user);
            removeItem(i);
            addUserArray(usersArray);
        }
        notifyDataSetChanged();
        Log.i("myrecycler","i size is" + i);
    }
    public Users_Array getUserArray(int index){
       if(data!=null && data.size()>index)
           return data.get(index);
        return null;
    }
    public void addUser(User user){
        if(data==null){
            data=new ArrayList<>();
            data.add(new Users_Array(user));
            notifyItemInserted(data.size());
        }else
        {
            if(!data.contains(new Users_Array(user))){
                data.add(new Users_Array(user));
                notifyItemInserted(data.size());
            }
        }

    }
    public void addUserArray(Users_Array users_array) {
        if (data != null) {
            if (!data.contains(users_array)) {
                data.add(users_array);
            }
        } else {
            data = new ArrayList<>();
            data.add(users_array);
        }
        notifyItemInserted(data.size());
    }

    public int getUserIndex(User user){
      return data.indexOf(user);

    }

    public void removeItem(int position){
        if(data!=null && data.size()>position){
            data.remove(position);
            notifyItemRemoved(position);
        }
        }


    public void addVehicle(Vehicle vehicle){
        data.add(new Users_Array(vehicle));
        notifyItemInserted(data.size());
    }

    public Users_Array getItem(int position){
        if(position<data.size())
            return data.get(position);
        return null;
    }
    public void switch_loading(){
        isUsers=false;
    }
    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private BoomMenuButton bmb;
        private TextView labelTextView;
        private TextView descriptionTextView;
        private ImageView profilImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.bmb = (BoomMenuButton) itemView.findViewById(R.id.bmb);
            labelTextView=(TextView) itemView.findViewById(R.id.card_label);
            descriptionTextView=(TextView) itemView.findViewById(R.id.card_desc);
            profilImageView=(ImageView)itemView.findViewById(R.id.card_img);
        }
    }
}


