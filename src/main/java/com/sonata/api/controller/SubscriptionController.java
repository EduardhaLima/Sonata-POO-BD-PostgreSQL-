package com.sonata.api.controller;

import com.sonata.api.model.Subscription;
import com.sonata.api.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST de assinaturas  - /api/subscriptions. */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public Subscription get(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    /** Assina/atualiza plano. Body: {"plan":"PREMIUM"} */
    @PostMapping("/user/{userId}")
    public Subscription subscribe(@PathVariable Long userId,
                                  @RequestParam Subscription.Plan plan) {
        return service.subscribe(userId, plan);
    }

    @DeleteMapping("/user/{userId}")
    public Subscription cancel(@PathVariable Long userId) {
        return service.cancel(userId);
    }
}
