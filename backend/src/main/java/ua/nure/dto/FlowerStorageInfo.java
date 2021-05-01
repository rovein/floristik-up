package ua.nure.dto;

import java.util.Date;

public interface FlowerStorageInfo {

    public Long getId();

    public Date getStartDate();

    public Integer getAmount();

    public String getFlowerName();

    public String getFlowerColor();

    public String getFlowerShelfLife();

    public Long getFlowerId();

    public Long getStorageRoomId();

    public Long getMinTemperature();

    public Long getMaxTemperature();

    public String getCity();

    public String getStreet();

    public String getHouse();

    public Long getMaxCapacity();

    public Integer getActualCapacity();

    public Long getTemperature();

    public Long getHumidity();
}
