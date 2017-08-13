package com.jeomix.android.gpstracker.files;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.jeomix.android.gpstracker.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.BoomPiece;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jeomix on 8/12/17.
 */


public class MyRecycle_Adapter extends RecyclerView.Adapter<MyRecycle_Adapter.MyViewHolder> {
    private  ArrayList<Users_Array> data=null;
    private static boolean isUsers=true;
    private static String ADMIN="Admin",PENDING_ADMIN="Admin Pending Approval",BANNED="Banned";
    public MyRecycle_Adapter(Users_Array entry) {
        data=new ArrayList<>();
        data.add(entry);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bmb.clearBuilders();
        for (int i = 0; i < holder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.if_car)
                    .normalText("Butter Doesn't fly!")
                    .subNormalText("Little butter Doesn't fly, either!") .rippleEffect(true)

                    // The color of boom-button when it is at normal-state.
                    ;
            holder.bmb.addBuilder(builder);
        }
        Users_Array currentUser=data.get(position);
        User user = currentUser.getUser();
        Vehicle v = currentUser.getVehicle();
        if(isUsers){
            switch( user.getIsAdmin()){
                case 0:
                    int imgRes=vehicleImage(v);
                    Glide.with(holder.itemView.getContext()).load(imgRes).placeholder(imgRes).into(holder.profilImageView);
                    holder.labelTextView.setText(v.getLabel());
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.material_blue_500));
                    holder.descriptionTextView.setText(v.getVin());
                    break;
                case 1 :
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(user.getEmail().charAt(0)), color);
                    holder.profilImageView.setImageDrawable(drawable);
                    holder.labelTextView.setText(ADMIN);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.firey));
                    holder.descriptionTextView.setText(user.getEmail());
                    break;
                case 2:
                    Glide.with(holder.itemView.getContext()).load(R.drawable.unchecked).placeholder(R.drawable.unchecked).into(holder.profilImageView);
                    holder.labelTextView.setText(PENDING_ADMIN);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.cold));
                    holder.descriptionTextView.setText(user.getEmail());
                    break;
                case 3:
                    Glide.with(holder.itemView.getContext()).load(R.drawable.attention).placeholder(R.drawable.attention).into(holder.profilImageView);
                    holder.labelTextView.setText(BANNED);
                    holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.red));
                    holder.descriptionTextView.setText(user.getEmail());
                    break;
            }

        }else{
            if(user.getIsAdmin()==0){
                int imgRes=vehicleImage(v);
                Glide.with(holder.itemView.getContext()).load(imgRes).placeholder(imgRes).into(holder.profilImageView);
                holder.labelTextView.setText(v.getLabel());
                holder.labelTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.material_blue_500));
                holder.descriptionTextView.setText(v.getVin());
            }
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
    public void addUser(User user){
        data.add(new Users_Array(user));
        notifyItemInserted(data.size());
    }

    public void removeItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
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
        isUsers=!isUsers;
    }
    @Override
    public int getItemCount() {
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


