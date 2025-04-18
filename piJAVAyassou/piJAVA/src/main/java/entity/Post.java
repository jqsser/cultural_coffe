package entity;
import java.sql.Date ;

public class Post {
    private int id;
    private int user_id;
    private String description_post;
    private Date date_post;
    private int nbr_likes ;
    private String image;
    private boolean is_validated;
    public Post() {
    }

    public Post(int user_id, String description_post, Date date_post, String image, boolean is_validated) {
        this.user_id = user_id;
        this.description_post = description_post;
        this.date_post = date_post;
        this.nbr_likes = nbr_likes;
        this.image = image;
        this.is_validated = is_validated;
    }
    public Post(int id, int user_id, String description_post, Date date_post, int nbr_likes, String image, boolean is_validated) {
        this.id = id;
        this.user_id = user_id;
        this.description_post = description_post;
        this.date_post = date_post;
        this.nbr_likes = this.nbr_likes;
        this.image = image;
        this.is_validated = is_validated;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return user_id;
    }

    public void setId_user(int id_user) {
        this.user_id = id_user;
    }

    public String getDescription_post() {
        return description_post;
    }

    public void setDescription_post(String description_post) {
        this.description_post = description_post;
    }

    public Date getDate_post() {
        return date_post;
    }

    public void setDate_post(Date date_post) {
        this.date_post = date_post;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIs_validated() {
        return is_validated;
    }

    public void setIs_validated(boolean is_validated) {
        this.is_validated = is_validated;
    }

    public int getNbr_likes() {
        return nbr_likes;
    }


    public void setNbr_likes(int nbr_likes) {
        this.nbr_likes = nbr_likes;
    }
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", id_user=" + user_id +
                ", description_post='" + description_post + '\'' +
                ", date_post=" + date_post +
                ", nbr_likes=" + nbr_likes + '\''  +
                ", image='" + image + '\'' +
                ", is_validated=" + is_validated +
                '}';
    }
}
