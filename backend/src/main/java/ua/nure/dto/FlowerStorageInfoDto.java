package ua.nure.dto;

import java.util.Date;

public interface FlowerStorageInfoDto {

    public Long getId();

    public Date getStartDate();

    public Integer getAmount();

    public String getFlowerName();

    public String getFlowerColor();

    public String getFlowerShelfLife();

    public Long getFlowerId();

    public Long getStorageRoomId();
}
