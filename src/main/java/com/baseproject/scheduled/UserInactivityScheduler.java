package com.baseproject.scheduled;

import com.baseproject.model.User;
import com.baseproject.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class UserInactivityScheduler {

  private final UserService userService;

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void deactivateInactiveUsers() {
    log.info("Starting the process to deactivate and delete inactive users");

    LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
    List<User> inactiveUsers = userService.findInactiveUsers(oneYearAgo);

    log.info("Found {} inactive users to deactivate", inactiveUsers.size());

    for (User user : inactiveUsers) {
      user.setIsActive(false);
      userService.save(user);
      log.info("User {} has been deactivated due to inactivity", user.getUsername());
    }

    LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
    List<User> usersToDelete = userService.findUsersToDelete(twoYearsAgo);

    log.info("Found {} users to delete after deactivation", usersToDelete.size());

    for (User user : usersToDelete) {
      userService.save(user);
      log.info("User {} has been deleted due to inactivity for more than two years", user.getUsername());
    }

    log.info("Inactive users deactivation and deletion process completed");
  }
}
