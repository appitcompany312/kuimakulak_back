package org.appitcompany.kuimakulak.entity;



import jakarta.persistence.*;
import lombok.*;
import org.appitcompany.kuimakulak.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private LocalDate joinedDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

   @OneToMany(mappedBy = "user")
   private List<Favorite> favorites;

   @OneToMany(mappedBy = "user")
   private List<History> history;

   @ManyToOne
   private Listeners listeners;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
}
