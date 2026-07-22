package com.github.BMP29.oauth2_auth_provider.service.Impl;

import com.github.BMP29.oauth2_auth_provider.dto.SignUpDto;
import com.github.BMP29.oauth2_auth_provider.dto.VerifyUserDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;
import com.github.BMP29.oauth2_auth_provider.repository.UserRepository;
import com.github.BMP29.oauth2_auth_provider.service.IAuthenticationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailServiceImpl emailService;

    @Override
    public User signup(SignUpDto signUpInput) {
        Optional<User> optionalUser =
                userRepository.findByUsername(signUpInput.username());

        if(optionalUser.isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        Optional<User> optionalEmail =
                userRepository.findByEmail(signUpInput.email());

        if (optionalEmail.isPresent()) {
            throw new RuntimeException("Email já está sendo usado");
        }

        User user = new User();
        user.setUsername(signUpInput.username());
        user.setEmail(signUpInput.email());
        user.setPassword(passwordEncoder.encode(signUpInput.password()));
        user.setEnabled(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setVerificationCode(UUID.randomUUID().toString());
        sendVerificationEmail(user);

        return userRepository.save(user);
    }

    @Override
    public void verify(VerifyUserDto verifyUserDto) {
        User user = userRepository.findByEmail(verifyUserDto.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código de verificação expirou.");
        }

        if(!user.getVerificationCode().equals(verifyUserDto.verificationCode())) {
            throw new RuntimeException("Código de verificação inválido.");
        }

        user.setEnabled(true);
        user.setLastUpdated(LocalDateTime.now());
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }


    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f3f3f3; margin: 0; padding: 0;\">"
                + "<div style=\"width: 100%; max-width: 600px; margin: 0 auto; padding: 30px;\">"
                + "<div style=\"text-align: center; background-color: #4CAF50; color: #fff; padding: 20px; border-radius: 8px 8px 0 0;\">"
                + "<h2 style=\"margin: 0; font-size: 24px;\">Bem-vindo ao nosso App!</h2>"
                + "<p style=\"font-size: 16px;\">Estamos felizes em tê-lo conosco. Complete o processo abaixo para continuar.</p>"
                + "</div>"
                + "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 0 0 8px 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\">"
                + "<h3 style=\"color: #333; font-size: 22px; margin-bottom: 15px;\">Código de Verificação</h3>"
                + "<p style=\"font-size: 16px; color: #555;\">Digite o código de verificação abaixo para continuar:</p>"
                + "<div style=\"text-align: center; margin-top: 20px;\">"
                + "<span style=\"font-size: 28px; font-weight: bold; color: #4CAF50; padding: 10px 20px; background-color: #f1f1f1; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">"
                + verificationCode
                + "</span>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #888; margin-top: 30px;\">Se você não reconhece essa solicitação, ignore este email.</p>"
                + "</div>"
                + "<div style=\"text-align: center; margin-top: 40px; font-size: 12px; color: #999;\">"
                + "<p>&copy; 2026, Nossa Empresa. Todos os direitos reservados.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //fazer: endpoint para verificar a conta
    //fazer: endpoint para registrar clients
}
