package crdma.genxcoders.com.disasterapp.models;

public class Event {
    private String disaster, status;

    public Event(String disaster, String status) {

        this.disaster = disaster;
        this.status = status;

    }



    public String getDisaster() {
        return disaster;
    }

    public void setDisaster(String name) {
        this.disaster = name;
    }

    public String status() {
        return status;
    }

    public void status(String status) {
        this.status = status;
    }


}
