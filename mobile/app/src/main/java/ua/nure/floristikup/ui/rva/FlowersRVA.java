package ua.nure.floristikup.ui.rva;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.edit.EditFlowerActivity;

public class FlowersRVA extends RecyclerView.Adapter<FlowersRVA.FlowersViewHolder>{

    private final Context context;
    private FlowersViewHolder flowersViewHolder;
    private JSONPlaceHolderApi apiService;
    private final List<Flower> flowers;
    private String token;

    public FlowersRVA(Context context, List<Flower> servicesList){
        this.context = context;
        this.flowers = servicesList;
    }

    @NotNull
    @Override
    public FlowersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flowers_card, viewGroup, false);
        apiService = NetworkService.getInstance().getApiService();
        token = "Bearer " + FloristShop.getInstance().getToken();
        return new FlowersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowersViewHolder holder, int position) {
        holder.cardView.setId(flowers.get(position).getId());
        holder.nameTV.setText(flowers.get(position).getName());
        holder.colorTV.setText(flowers.get(position).getColor());
        holder.shelfLifeTV.setText(Integer.toString(flowers.get(position).getShelfLife()));
        holder.minTempTV.setText( Integer.toString(flowers.get(position).getMinTemperature()));
        holder.maxTempTV.setText(Integer.toString(flowers.get(position).getMaxTemperature()));

        holder.editButton.setOnClickListener(v -> editFlower(holder));

        holder.deleteButton.setOnClickListener(v -> {
            flowersViewHolder = holder;
            deleteFlower();
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return flowers.size();
    }

    private void editFlower(FlowersViewHolder holder) {
        Intent intent = new Intent(context, EditFlowerActivity.class);
        intent.putExtra("fId", holder.cardView.getId());
        context.startActivity(intent);
    }

    private void deleteFlower() {
        apiService.deleteFlower(token, flowersViewHolder.cardView.getId()).enqueue(deleteCallback);
    }

    private final Callback<Flower> deleteCallback = new Callback<Flower>() {
        @Override
        public void onResponse(Call<Flower> call, Response<Flower> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                flowers.remove(flowersViewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<Flower> call, Throwable t) {
            System.out.println(t);
        }
    };

    static class FlowersViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nameTV, colorTV, shelfLifeTV, minTempTV, maxTempTV;
        Button editButton, deleteButton;

        FlowersViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.flowers_card_view);
            nameTV = itemView.findViewById(R.id.name_text);
            colorTV = itemView.findViewById(R.id.color_text);
            shelfLifeTV = itemView.findViewById(R.id.shelf_life_text);
            minTempTV = itemView.findViewById(R.id.flower_name_text);
            maxTempTV = itemView.findViewById(R.id.max_temp_text);

            editButton = itemView.findViewById(R.id.edit_flower_btn);
            deleteButton = itemView.findViewById(R.id.delete_flower_btn);
        }

    }

}


