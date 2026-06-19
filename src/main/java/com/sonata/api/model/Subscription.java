package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Subscription (Assinatura/Plano do usuario).
 *
 * <p>Faz total sentido para o Sonata: o servico promete audio
 * <b>Lossless/FLAC</b> como diferencial. Quem pode tocar nessa qualidade
 * eh definido pelo plano contratado.</p>
 *
 * <p>Relacao 1:1 com {@link User}.</p>
 */
@Entity
@Table(name = "subscription")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Subscription {

    /** Tipos de plano suportados pelo Sonata. */
    public enum Plan {
        /** Gratuito - audio em qualidade padrao, com limites. */
        FREE,
        /** Pago - alta qualidade, sem anuncios. */
        PREMIUM,
        /** Top de linha - audio Lossless/FLAC, modo offline. */
        LOSSLESS
    }

    public enum Status { ACTIVE, CANCELLED, EXPIRED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    /** Usuario dono do plano (1:1). */
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Plan plan;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Pode ser null para planos vitalicios/teste. */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** Regra de negocio: o plano permite audio Lossless? */
    public boolean allowsLossless() {
        return plan == Plan.LOSSLESS;
    }
}
