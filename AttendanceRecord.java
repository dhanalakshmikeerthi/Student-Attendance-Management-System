public class AttendanceRecord {

    private String roll;
    private String subject;
    private String date;
    private boolean present;

    public AttendanceRecord(String roll,String subject,String date,boolean present){

        this.roll = roll;
        this.subject = subject;
        this.date = date;
        this.present = present;
    }

    public String getRoll(){return roll;}
    public String getSubject(){return subject;}
    public String getDate(){return date;}
    public boolean isPresent(){return present;}

}