package com.canteen.model;

public class MenuItem {

    private Long    id;
    private String  name;
    private String  category;   // Main Course | Snacks | Beverages | Drinks
    private int     price;
    private String  emoji;
    private boolean veg;
    private boolean available;

    // ── Constructors ──────────────────────────────────────────────────
    public MenuItem() {
        this.available = true;
    }

    public MenuItem(Long id, String name, String category, int price, String emoji, boolean veg) {
        this.id        = id;
        this.name      = name;
        this.category  = category;
        this.price     = price;
        this.emoji     = emoji;
        this.veg       = veg;
        this.available = true;
    }

    // ── Getters & Setters ─────────────────────────────────────────────
    public Long   getId()           { return id; }
    public void   setId(Long id)    { this.id = id; }

    public String getName()             { return name; }
    public void   setName(String name)  { this.name = name; }

    public String getCategory()                 { return category; }
    public void   setCategory(String category)  { this.category = category; }

    public int  getPrice()           { return price; }
    public void setPrice(int price)  { this.price = price; }

    public String getEmoji()              { return emoji; }
    public void   setEmoji(String emoji)  { this.emoji = emoji; }

    public boolean isVeg()             { return veg; }
    public void    setVeg(boolean veg) { this.veg = veg; }

    public boolean isAvailable()                  { return available; }
    public void    setAvailable(boolean available) { this.available = available; }
}
