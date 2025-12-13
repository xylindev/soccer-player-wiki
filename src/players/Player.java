package players;

import java.time.LocalDate;

public class Player {
    private String name;
    private String firstname;
    private String position;
    private int age;
    private final String NATIONALITY;
    private String team;
    private int jersey;
    private int height;
    private final LocalDate BIRTHDATE;
    private final String BIRTHPLACE;
    private String strongFoot;
    private final String PATH;

    public Player(String name, String firstname, String position, int age, String nationality, String team, int jersey, int height, LocalDate birthDate, String birthPlace, String strongFoot, String path){
        this.name = name;
        this.firstname = firstname;
        this.position = position;
        this.age = age;
        this.NATIONALITY = nationality;
        this.team = team;
        this.jersey = jersey;
        this.height = height;
        this.BIRTHDATE = birthDate;
        this.BIRTHPLACE = birthPlace;
        this.strongFoot = strongFoot;
        this.PATH = path;
    }

    public Player(String[] data){
        this(
            data[0], 
            data[1], 
            data[2], 
            Integer.parseInt(data[3]), 
            data[4], 
            data[5], 
            Integer.parseInt(data[6]), 
            Integer.parseInt(data[7]), 
            LocalDate.parse(changeDateFormat(data[8])), 
            data[9], 
            data[10], 
            data[11]
        );
    }

    // ------------------------------- UTILS -------------------------------
    private static String changeDateFormat(String date) {
        StringBuilder newFormat = new StringBuilder();

        String day = date.substring(0, 2);
        String month = date.substring(3,5);
        String year = date.substring(6, date.length());

        newFormat.append(year + "-" + month + "-" + day);

        return newFormat.toString();
    }

    // ------------------------- GETTERS & SETTERS -------------------------

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPosition() {
        return position;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return NATIONALITY;
    }

    public String getTeam() {
        return team;
    }

    public int getJersey() {
        return jersey;
    }

    public int getHeight() {
        return height;
    }

    public LocalDate getBIRTHDATE() {
        return BIRTHDATE;
    }

    public String getBIRTHPLACE() {
        return BIRTHPLACE;
    }

    public String getStrongFoot() {
        return strongFoot;
    }

    public String getPATH() {
        return PATH;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setJersey(int jersey) {
        this.jersey = jersey;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setStrongFoot(String strongFoot) {
        this.strongFoot = strongFoot;
    }
}
