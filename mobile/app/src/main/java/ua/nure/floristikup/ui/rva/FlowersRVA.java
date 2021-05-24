package ua.nure.floristikup.ui.rva;

import android.app.Activity;
import android.app.AlertDialog;
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
import ua.nure.floristikup.ui.util.LoadingDialog;

public class FlowersRVA extends RecyclerView.Adapter<FlowersRVA.FlowersViewHolder>{

    private final Context context;
    private FlowersViewHolder flowersViewHolder;
    private JSONPlaceHolderApi apiService;
    private final List<Flower> flowers;
    private String token;
    LoadingDialog loadingDialog;

    public FlowersRVA(Context context, List<Flower> servicesList){
        this.context = context;
        this.flowers = servicesList;
        this.loadingDialog = new LoadingDialog((Activity) context);
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
        Flower flower = flowers.get(position);
        String nameAndColor = flower.getName() + ", " + flower.getColor();

        holder.cardView.setId(flower.getId());
        holder.nameTV.setText(flower.getName());
        holder.colorTV.setText(flower.getColor());
        holder.shelfLifeTV.setText(Integer.toString(flower.getShelfLife()));
        holder.minTempTV.setText( Integer.toString(flower.getMinTemperature()));
        holder.maxTempTV.setText(Integer.toString(flower.getMaxTemperature()));
        holder.flowerNameLabel.setText(nameAndColor);

        holder.editButton.setOnClickListener(v -> editFlower(holder));

        holder.deleteButton.setOnClickListener(v -> {
            flowersViewHolder = holder;
            new AlertDialog.Builder(this.context)
                    .setTitle(context.getString(R.string.delete))
                    .setMessage(context.getString(R.string.are_you_sure_delete))
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteFlower())
                    .setNegativeButton(android.R.string.no, null).show();
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
        loadingDialog.start();
        apiService.deleteFlower(token, flowersViewHolder.cardView.getId()).enqueue(deleteCallback);
    }

    private final Callback<Flower> deleteCallback = new Callback<Flower>() {
        @Override
        public void onResponse(Call<Flower> call, Response<Flower> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                flowers.remove(flowersViewHolder.getAdapterPosition());
                notifyItemRemoved(flowersViewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<Flower> call, Throwable t) {
            System.out.println(t);
            flowers.remove(flowersViewHolder.getAdapterPosition());
            notifyItemRemoved(flowersViewHolder.getAdapterPosition());
            loadingDialog.dismiss();
        }
    };

    static class FlowersViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nameTV, colorTV, shelfLifeTV, minTempTV, maxTempTV, flowerNameLabel;
        Button editButton, deleteButton;

        FlowersViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.flowers_card_view);
            nameTV = itemView.findViewById(R.id.name_text);
            colorTV = itemView.findViewById(R.id.color_text);
            shelfLifeTV = itemView.findViewById(R.id.shelf_life_text);
            minTempTV = itemView.findViewById(R.id.flower_name_text);
            maxTempTV = itemView.findViewById(R.id.max_temp_text);
            flowerNameLabel = itemView.findViewById(R.id.flower_name_label);

            editButton = itemView.findViewById(R.id.edit_flower_btn);
            deleteButton = itemView.findViewById(R.id.delete_flower_btn);
        }

    }

}


