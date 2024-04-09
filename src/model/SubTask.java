package model;

public class SubTask extends Task {

    // id эпика, в котором хранится этот subTask
    private int epicId;


    public SubTask(String name, Status status, String description) {
        super(name, status, description);
    }

    public SubTask(int epicId, String name, Status status, String description) {
        super(name, status, description);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return this.epicId;
    }

    public void setEpicId(Epic epic) {
        this.epicId = epic.getId();
    }
}
