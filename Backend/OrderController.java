package com.canteen.controller;

import com.canteen.model.Order;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    // In-memory store — replace with JPA repository for 100%
    private final List<Order> orderStore    = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger idCounter   = new AtomicInteger(1001);

    // ── POST /api/orders — Place a new order ──────────────────────────
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        order.setId("ORD-" + idCounter.getAndIncrement());

        if (order.getStudentName() == null || order.getStudentName().isBlank()) {
            order.setStudentName("student");
        }
        if (order.getStatus() == null) {
            order.setStatus("Pending");
        }

        orderStore.add(order);

        System.out.println("[ORDER PLACED] " + order.getId()
            + " | Student: " + order.getStudentName()
            + " | Total: Rs." + order.getTotal());

        return ResponseEntity.ok("Order Placed Successfully! Order ID: " + order.getId());
    }

    // ── GET /api/orders — Get all orders (with optional status filter) ─
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) String status) {

        List<Order> result = orderStore.stream()
            .filter(o -> status == null || o.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ── GET /api/orders/{id} — Get single order ───────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        return orderStore.stream()
            .filter(o -> o.getId().equals(id))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ── PATCH /api/orders/{id}/status — Update order status ──────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String newStatus = body.get("status");
        if (newStatus == null) {
            return ResponseEntity.badRequest().body("Missing 'status' field");
        }

        List<String> allowed = Arrays.asList("Pending", "Preparing", "Ready", "Cancelled");
        if (!allowed.contains(newStatus)) {
            return ResponseEntity.badRequest().body("Invalid status: " + newStatus);
        }

        return orderStore.stream()
            .filter(o -> o.getId().equals(id))
            .findFirst()
            .map(order -> {
                String old = order.getStatus();
                order.setStatus(newStatus);
                System.out.println("[STATUS] " + id + ": " + old + " -> " + newStatus);
                return ResponseEntity.ok("Status updated: " + old + " -> " + newStatus);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/orders/{id} — Delete an order ─────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        boolean removed = orderStore.removeIf(o -> o.getId().equals(id));
        return removed
            ? ResponseEntity.ok("Order " + id + " removed")
            : ResponseEntity.notFound().build();
    }

    // ── GET /api/orders/stats — Summary stats for admin dashboard ─────
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total",     orderStore.size());
        stats.put("pending",   orderStore.stream().filter(o -> "Pending".equals(o.getStatus())).count());
        stats.put("preparing", orderStore.stream().filter(o -> "Preparing".equals(o.getStatus())).count());
        stats.put("ready",     orderStore.stream().filter(o -> "Ready".equals(o.getStatus())).count());
        stats.put("cancelled", orderStore.stream().filter(o -> "Cancelled".equals(o.getStatus())).count());
        stats.put("revenue",   orderStore.stream()
            .filter(o -> !"Cancelled".equals(o.getStatus()))
            .mapToInt(Order::getTotal).sum());
        return ResponseEntity.ok(stats);
    }
}
