package com.tpe.entity.concretes.user;

import com.tpe.entity.concretes.business.Loan;
import com.tpe.entity.concretes.business.Role;
import com.tpe.entity.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(nullable = false)
    @Min(value = -2)
    @Max(value = 2)
    private Integer score = 0;

    @NotNull
    @Size(min = 10, max = 100)
    private String address;

    @NotNull
    @Pattern(regexp = "\\d{3} \\d{3} \\d{4}")
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull
    @Email
    @Size(min = 10, max = 80)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private LocalDateTime createDate;

    private String resetPasswordCode;

    @NotNull
    private Boolean builtIn = false;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;



}
