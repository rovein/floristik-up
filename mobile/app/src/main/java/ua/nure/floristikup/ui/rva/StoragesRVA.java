package ua.nure.floristikup.ui.rva;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FlowerStorage;
import ua.nure.floristikup.util.DateUtil;

public class StoragesRVA extends RecyclerView.Adapter<StoragesRVA.ContractsViewHolder>{

    private Context mContext;
    private final List<FlowerStorage> mFlowerStorages;

    public StoragesRVA(Context context, List<FlowerStorage> flowerStorages){
        this.mContext = context;
        this.mFlowerStorages = flowerStorages;
    }

    @NotNull
    @Override
    public ContractsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storage_card, viewGroup, false);
        return new ContractsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ContractsViewHolder holder, int position) {
        FlowerStorage storageInfo = mFlowerStorages.get(position);

        holder.cardView.setId(storageInfo.getId());
        holder.storageInfoHeaderTV.setText(storageInfo.getFlowerName() + ", " + storageInfo.getFlowerColor());

        Date startDate = storageInfo.getStartDate();
        int flowerShelfLife = Integer.parseInt(storageInfo.getFlowerShelfLife());
        Date today = new Date();
        if (today.before(DateUtil.addDays(startDate, flowerShelfLife))) {
            holder.shelfLifeInfoTV.setText(mContext.getString(R.string.normal_shelf_life));
            holder.shelfLifeInfoTV.setTextColor(Color.parseColor("#2e8b57"));
        } else {
            holder.shelfLifeInfoTV.setText(mContext.getString(R.string.expired_shelf_life));
            holder.shelfLifeInfoTV.setTextColor(Color.RED);
        }

        Double actualTemperature = storageInfo.getTemperature();
        Long minTemperature = storageInfo.getMinTemperature();
        Long maxTemperature = storageInfo.getMaxTemperature();
        if (actualTemperature >= minTemperature && actualTemperature <= maxTemperature) {
            holder.climateInfoTV.setText(mContext.getString(R.string.climate_is_normal));
            holder.climateInfoTV.setTextColor(Color.parseColor("#2e8b57"));
        } else {
            holder.climateInfoTV.setText(mContext.getString(R.string.climate_is_abnormal));
            holder.climateInfoTV.setTextColor(Color.RED);
        }

        holder.startDateTV.setText(storageInfo.getFormattedDate());
        holder.shelfLifeTV.setText(storageInfo.getFlowerShelfLife());
        holder.amountTV.setText(Integer.toString(storageInfo.getAmount()));
        holder.temperatureRangeTV.setText(minTemperature + " - " + maxTemperature + " °C");
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mFlowerStorages.size();
    }

    static class ContractsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView storageInfoHeaderTV, shelfLifeInfoTV, climateInfoTV, startDateTV, shelfLifeTV, amountTV, temperatureRangeTV;
        ContractsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.storage_cv);

            storageInfoHeaderTV = (TextView) itemView.findViewById(R.id.storage_info_header_text);
            shelfLifeInfoTV = (TextView) itemView.findViewById(R.id.shelf_life_info);
            climateInfoTV = (TextView) itemView.findViewById(R.id.climate_info);
            startDateTV = (TextView) itemView.findViewById(R.id.start_date_text);
            shelfLifeTV = (TextView) itemView.findViewById(R.id.shelf_life_text);
            amountTV = (TextView) itemView.findViewById(R.id.amount_text);
            temperatureRangeTV = (TextView) itemView.findViewById(R.id.temperature_text);
        }

    }

}


