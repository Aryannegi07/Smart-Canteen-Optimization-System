package com.canteen.controller;

import com.canteen.model.MenuItem;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    // In-memory menu — replace with JPA repository for 100%
    private final List<MenuItem> menuStore = Collections.synchronizedList(new ArrayList<>(Arrays.asList(
        new MenuItem(1L,  "Burger",       "Main Course", 50,  "🍔", true),
        new MenuItem(2L,  "Coffee",       "Beverages",   30,  "☕", true),
        new MenuItem(3L,  "Cold Drink",   "Drinks",      25,  "🥤", true),
        new MenuItem(4L,  "Pizza Slice",  "Main Course", 80,  "🍕", true),
        new MenuItem(5L,  "Samosa",       "Snacks",      15,  "🥟", true),
        new MenuItem(6L,  "Maggi",        "Main Course", 30,  "🍜", true),
        new MenuItem(7L,  "Tea",          "Beverages",   10,  "🍵", true),
        new MenuItem(8L,  "Sandwich",     "Snacks",      40,  "🥪", true),
        new MenuItem(9L,  "Chicken Roll", "Main Course", 70,  "🌯", false),
        new MenuItem(10L, "Juice",        "Drinks",      35,  "🧃", true)
    )));

    // ── GET /api/menu — Get all available menu items ──────────────────
    @GetMapping
    public ResponseEntity<List<MenuItem>> getMenu(
            @RequestParam(required = false) String category) {

        List<MenuItem> result = menuStore.stream()
            .filter(MenuItem::isAvailable)
            .filter(m -> category == null || m.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ── POST /api/menu — Add a new menu item (admin) ──────────────────
    @PostMapping
    public ResponseEntity<MenuItem> addItem(@RequestBody MenuItem item) {
        item.setId(System.currentTimeMillis());
        item.setAvailable(true);
        menuStore.add(item);
        System.out.println("[MENU] Item added: " + item.getName() + " Rs." + item.getPrice());
        return ResponseEntity.ok(item);
    }

    // ── DELETE /api/menu/{id} — Remove a menu item (admin) ───────────
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        boolean removed = menuStore.removeIf(m -> m.getId().equals(id));
        return removed
            ? ResponseEntity.ok("Item removed")
            : ResponseEntity.notFound().build();
    }

    // ── PUT /api/menu/{id} — Update price or availability (admin) ────
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        return menuStore.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .map(item -> {
                if (body.containsKey("price"))
                    item.setPrice(((Number) body.get("price")).intValue());
                if (body.containsKey("available"))
                    item.setAvailable((Boolean) body.get("available"));
                if (body.containsKey("name"))
                    item.setName((String) body.get("name"));
                return ResponseEntity.ok("Item updated");
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
