package com.sonata.api.service;

import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.Subscription;
import com.sonata.api.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/** Regras de negocio para Subscription . */
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final UserService userService;

    public SubscriptionService(SubscriptionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Subscription findByUser(Long userId) {
        return repository.findByUser_UserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Assinatura para usuario " + userId + " nao encontrada"));
    }

    /** Cria/atualiza o plano do usuario (upsert simples). */
    public Subscription subscribe(Long userId, Subscription.Plan plan) {
        Subscription sub = repository.findByUser_UserId(userId).orElseGet(() -> {
            Subscription s = new Subscription();
            s.setUser(userService.findById(userId));
            s.setStartDate(LocalDate.now());
            return s;
        });
        sub.setPlan(plan);
        sub.setStatus(Subscription.Status.ACTIVE);
        return repository.save(sub);
    }

    public Subscription cancel(Long userId) {
        Subscription sub = findByUser(userId);
        sub.setStatus(Subscription.Status.CANCELLED);
        sub.setEndDate(LocalDate.now());
        return repository.save(sub);
    }
}
