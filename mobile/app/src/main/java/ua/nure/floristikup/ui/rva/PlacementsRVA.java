package ua.nure.floristikup.ui.rva;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.edit.EditPlacementActivity;
import ua.nure.floristikup.ui.profile.MenuActivity;
import ua.nure.floristikup.ui.storages.StoragesActivity;

public class PlacementsRVA extends RecyclerView.Adapter<PlacementsRVA.RoomsViewHolder>{

    private final Context mContext;
    private RoomsViewHolder mRoomsViewHolder;
    private JSONPlaceHolderApi mApi;
    private String token;
    private final List<Placement> mPlacements;

    public PlacementsRVA(Context context, List<Placement> placementList){
        this.mContext = context;
        this.mPlacements = placementList;
    }

    @NotNull
    @Override
    public RoomsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.placement_card, viewGroup, false);
        mApi = NetworkService.getInstance().getApiService();
        token = "Bearer " + FloristShop.getInstance().getToken();
        return new RoomsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        Placement placement = mPlacements.get(position);
        holder.mCardView.setId(placement.getId());

        holder.placementNumberTV.setText(Integer.toString(placement.getId()));
        holder.cityTV.setText(placement.getCity());
        holder.streetTV.setText(placement.getStreet());
        holder.houseTV.setText(placement.getHouse());
        holder.temperatureTV.setText(Double.toString(placement.getSmartDevice().getTemperature()));
        holder.humidityTV.setText(Double.toString(placement.getSmartDevice().getHumidity()));
        holder.capacityTV.setText(placement.getActualCapacity() + "/" + placement.getMaxCapacity());

        holder.toStoragesButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StoragesActivity.class);
            intent.putExtra("rId", holder.mCardView.getId());
            mContext.startActivity(intent);
        });

        holder.mEditButton.setOnClickListener(v -> editRoom(holder));

        holder.mDeleteButton.setOnClickListener(v -> {
            mRoomsViewHolder = holder;
            deleteRoom();
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mPlacements.size();
    }

    private void editRoom(RoomsViewHolder holder) {
        Intent intent = new Intent(mContext, EditPlacementActivity.class);
        intent.putExtra("rId", holder.mCardView.getId());
        mContext.startActivity(intent);
    }

    private void deleteRoom() {
        mApi.deletePlacement(token, mRoomsViewHolder.mCardView.getId()).enqueue(deleteCallback);
    }

    Callback<Placement> deleteCallback = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, Response<Placement> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                mPlacements.remove(mRoomsViewHolder.getAdapterPosition());
                notifyItemRemoved(mRoomsViewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            System.out.println(t);
            mPlacements.remove(mRoomsViewHolder.getAdapterPosition());
            notifyItemRemoved(mRoomsViewHolder.getAdapterPosition());
        }
    };

    static class RoomsViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView placementNumberTV, cityTV, streetTV, houseTV, temperatureTV, humidityTV, capacityTV;
        Button mEditButton, mDeleteButton, toStoragesButton;

        RoomsViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.room_cv);

            placementNumberTV = itemView.findViewById(R.id.flower_name_text);
            cityTV = itemView.findViewById(R.id.city_text);
            streetTV = itemView.findViewById(R.id.street_text);
            houseTV = itemView.findViewById(R.id.house_text);
            temperatureTV = itemView.findViewById(R.id.temperature_text);
            humidityTV = itemView.findViewById(R.id.humidity_text);
            capacityTV = itemView.findViewById(R.id.capacity_text);

            mEditButton = itemView.findViewById(R.id.edit_room_btn);
            mDeleteButton = itemView.findViewById(R.id.delete_room_btn);
            toStoragesButton = itemView.findViewById(R.id.to_storages_button);
        }
    }
}
