package com.dark1103.velolist.models;

public class Park {
    private String Address;
    private int AvailableBikes;
    private int FreePlaces;

    private boolean IsLocked;

    private String Id;
    private boolean Selected;

    private String Name;

    public Park(String id, String address, int freePlaces, int availableBikes, boolean isLocked, boolean selected,
                String name) {
        this.Address = address;
        this.Id = id;
        this.FreePlaces = freePlaces;
        this.AvailableBikes = availableBikes;
        this.IsLocked = isLocked;
        this.Selected = selected;
        this.Name = name;
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String id) {
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

    public boolean isLocked() {
        return IsLocked;
    }

    public void setLocked(boolean locked) {
        IsLocked = locked;
    }

}
