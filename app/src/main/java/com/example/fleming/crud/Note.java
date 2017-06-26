package com.example.fleming.crud;

/**
 * Created by fleming on 3/12/16.
 */
public class Note {

    private int _id;
    private String _title;
    private String _content;

    /**
     * Constructor
     */
    public Note() {

    }

    /**
     * Constructor with all params
     *
     * @param id
     * @param title
     * @param content
     */
    public Note(int id, String title, String content) {
        this.setId(id);
        this.setTitle(title);
        this.setContent(content);
    }

    /**
     * Constructor without Id
     *
     * @param title
     * @param content
     */
    public Note(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }

    /**
     * Get id.
     *
     * @return (int)
     */
    public int getId() {
        return this._id;
    }

    /**
     * Set id.
     *
     * @param id
     */
    public void setId(int id) {
        this._id = id;
    }

    /**
     * Get title.
     *
     * @return (String)
     */
    public String getTitle() {
        return this._title;
    }

    /**
     * Set title.
     *
     * @param title
     */
    public void setTitle(String title) {
        this._title = title;
    }

    /**
     * Get content.
     *
     * @return (String)
     */
    public String getContent() {
        return this._content;
    }

    /**
     * Set content.
     *
     * @param content
     */
    public void setContent(String content) {
        this._content = content;
    }

    public String toString() {
        return this._content;
    }
}
