package user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String username;
    private String password;
    private List<String> favorite;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password, List<String> favorite){
        this(username, password);
        this.name = name;
        this.favorite = favorite;
    }

    public User(String name, String username, String password){
        this(name, username, password, null);
    }

    public User(String username, String password, List<String> favorite){
        this(null, username, password, favorite);
    }

    // -------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getFavorite() {
        return favorite;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavorite(List<String> favorite) {
        this.favorite = favorite;
    }
}
