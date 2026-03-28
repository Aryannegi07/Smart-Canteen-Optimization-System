package com.canteen.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {

    private String id;
    private String studentName;
    private List<OrderItem> items;
    private int    total;
    private String status;  // Pending | Preparing | Ready | Cancelled
    private String time;

    // ── Nested OrderItem class ────────────────────────────────────────
    public static class OrderItem {
        private String item;
        private int    qty;
        private int    price;

        public String getItem()            { return item;  }
        public void   setItem(String item) { this.item  = item;  }
        public int    getQty()             { return qty;   }
        public void   setQty(int qty)      { this.qty   = qty;   }
        public int    getPrice()           { return price; }
        public void   setPrice(int price)  { this.price = price; }
    }

    // ── Constructor ───────────────────────────────────────────────────
    public Order() {
        this.status = "Pending";
        this.time   = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    // ── Getters & Setters ─────────────────────────────────────────────
    public String getId()              { return id; }
    public void   setId(String id)     { this.id = id; }

    public String getStudentName()              { return studentName; }
    public void   setStudentName(String name)   { this.studentName = name; }

    public List<OrderItem> getItems()                      { return items; }
    public void            setItems(List<OrderItem> items) { this.items = items; }

    public int  getTotal()           { return total; }
    public void setTotal(int total)  { this.total = total; }

    public String getStatus()            { return status; }
    public void   setStatus(String s)    { this.status = s; }

    public String getTime()              { return time; }
    public void   setTime(String time)   { this.time = time; }
}
