package com.rt_chatApp.security.auditing;

import com.rt_chatApp.security.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/**
 * Provides the current auditor (user ID) for auditing purposes.
 *
 * <p>This class is used by Spring Data JPA's auditing feature to automatically populate
 * audit-related fields such as {@code createdBy} and {@code lastModifiedBy} with the
 * currently authenticated user's ID.</p>
 *
 * <p>The auditor is retrieved from the {@link SecurityContextHolder}, ensuring that
 * only authenticated users' IDs are used for auditing. If no authenticated user is found,
 * an empty {@link Optional} is returned.</p>
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
