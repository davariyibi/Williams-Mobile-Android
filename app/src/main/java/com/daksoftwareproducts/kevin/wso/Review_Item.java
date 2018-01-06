package com.daksoftwareproducts.kevin.wso;

/**
 * Created by Kevin on 8/9/2017.
 */

public class Review_Item {

    private String courseTitle;
    private String comment;
    private String rating;
    private String datePosted;

    // Getter and Setter for courseTitle
    public String getCourseTitle() {
        return courseTitle;
    }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    // Getter and Setter for comment
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter and Setter for rating
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    // Getter and Setter for datePosted
    public String getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

}
