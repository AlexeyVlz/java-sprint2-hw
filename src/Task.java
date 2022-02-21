public class Task {
    private String title ="";
    private String specification;
    private String status;

    public Task(String title, String specification, String status) {
        this.title = title;
        this.specification = specification;
        this.status = status;
    }

    public Task(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", specification='" + specification + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
