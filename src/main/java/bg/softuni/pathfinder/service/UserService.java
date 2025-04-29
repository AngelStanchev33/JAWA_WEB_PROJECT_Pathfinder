package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.entity.UserEntity;
import bg.softuni.pathfinder.model.binding.LoginBindingModel;
import bg.softuni.pathfinder.model.binding.RegisterBindingModel;
import bg.softuni.pathfinder.model.dto.UserProfileDTO;
import bg.softuni.pathfinder.model.entity.enums.RoleEnum;
import bg.softuni.pathfinder.reposityory.UserRepo;
import bg.softuni.pathfinder.session.CurrentUser;
import bg.softuni.pathfinder.model.service.UserServiceModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final CurrentUser currentUser;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, CurrentUser currentUser, ModelMapper modelMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.currentUser = currentUser;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean loginUser(LoginBindingModel loginBindingModel) {
        Optional<UserEntity> userOptional = userRepo.findByUsername(loginBindingModel.getUserName());

        if (userOptional.isEmpty()) {
            return false;
        }

        UserEntity user = userOptional.get();

        // Check if password is already encrypted (starts with $2a$)
        if (!user.getPassword().startsWith("$2a$")) {
            // Password is not encrypted, check if it matches directly
            if (!user.getPassword().equals(loginBindingModel.getPassword())) {
                return false;
            }
            // If password matches, encrypt it and save
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
        } else {
            // Password is already encrypted, use passwordEncoder.matches
            if (!passwordEncoder.matches(loginBindingModel.getPassword(), user.getPassword())) {
                return false;
            }
        }

        currentUser.setId(user.getId());
        currentUser.setFullName(user.getFullName());
        currentUser.setLogged(true);
        if (isAdmin(user)) {
            currentUser.setAdmin(true);
        }

        System.out.println("User logged in: " + currentUser.getFullName());

        return true;
    }

    public boolean isAdmin(UserEntity user) {
        return userRepo
                .findByUsernameAndRoles_Name(user.getUsername(), RoleEnum.ADMIN)
                .isPresent();
    }

    public void logOutUser() {
        this.currentUser.clear();
    }

    public boolean registerUser(RegisterBindingModel registerBindingModel) {
        UserServiceModel userServiceModel = modelMapper.map(registerBindingModel, UserServiceModel.class);

        if (userRepo.findByUsername(userServiceModel.getUsername()).isPresent()) {
            return false;
        }

        UserEntity user = modelMapper.map(userServiceModel, UserEntity.class);

        userRepo.save(user);
        return true;
    }

    public UserEntity findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfileDTO getProfileData(Long id) {
        UserEntity user = findById(id);
        UserProfileDTO userProfileDTO = modelMapper.map(user, UserProfileDTO.class);
        userProfileDTO.setLevel(user.getLevel().name());
        return userProfileDTO;
    }

}
