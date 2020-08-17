package com.dark1103.velolist.models;

public class Park {
    private String Address;
    private int AvailableBikes;
    private int FreePlaces;

    private Integer Id;
    private boolean Selected;

    private String Name;

    public Park(Integer id, String address, int freePlaces, int avaibleBikes, boolean selected, String name) {
        this.Address = address;
        this.Id = id;
        this.FreePlaces = freePlaces;
        this.AvailableBikes = avaibleBikes;
        this.Selected = selected;
        this.Name = name;
    }

    public Integer getId() {
        return this.Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public int getFreePlaces() {
        return this.FreePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.FreePlaces = freePlaces;
    }

    public int getAvailableBikes() {
        return this.AvailableBikes;
    }

    public void setAvailableBikes(int availableBikes) {
        this.AvailableBikes = availableBikes;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String mAddress) {
        this.Address = mAddress;
    }

    public boolean isSelected() {
        return this.Selected;
    }

    public void setSelected(boolean selected) {
        this.Selected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
