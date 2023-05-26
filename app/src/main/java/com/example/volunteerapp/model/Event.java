package com.example.volunteerapp.model;

public class Event {
    private String title, date, organizer, venue, division_id, direction_id;
    private int quantity_max, quantity_current;
    private boolean report;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDivision_id() {
        return division_id;
    }

    public void setDivision_id(String division_id) {
        this.division_id = division_id;
    }

    public String getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(String direction_id) {
        this.direction_id = direction_id;
    }

    public int getQuantity_max() {
        return quantity_max;
    }

    public void setQuantity_max(int quantity_max) {
        this.quantity_max = quantity_max;
    }

    public int getQuantity_current() {
        return quantity_current;
    }

    public void setQuantity_current(int quantity_current) {
        this.quantity_current = quantity_current;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }
}
