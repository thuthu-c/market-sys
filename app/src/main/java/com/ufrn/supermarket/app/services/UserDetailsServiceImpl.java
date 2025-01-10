import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Simulando um usuário de banco de dados
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{bcrypt}$2a$10$D1hZkOExHqBqEZMycUcxXXvmpd8QJGNVuIjAf4a6/xewJ/6Fw57bC") // Senha: "admin"
                    .roles("ADMIN")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
