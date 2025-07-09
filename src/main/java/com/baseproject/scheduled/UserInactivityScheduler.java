package com.baseproject.scheduled;

import com.baseproject.exception.BaseException;
import com.baseproject.model.User;
import com.baseproject.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.baseproject.exception.ExceptionEnum.UNKNOW_ERROR;

@Component
@Slf4j
public class UserInactivityScheduler implements SchedulingConfigurer {

  private final String inactivityCheckInterval;
  private final Integer inactivityThresholdDays;
  private final Integer deletionThresholdDays;
  private final UserService userService;

  public UserInactivityScheduler(
      @Value("${scheduler.user.user-inactivity-check-interval:0 0 0 * * ?}") String inactivityCheckInterval,
      @Value("${scheduler.user.user-deactivation-days:365}") Integer inactivityThresholdDays,
      @Value("${scheduler.user.user-delete-days:730}") Integer deletionThresholdDays,
      UserService userService
  ) {
    this.inactivityCheckInterval = inactivityCheckInterval;
    this.inactivityThresholdDays = inactivityThresholdDays;
    this.deletionThresholdDays = deletionThresholdDays;
    this.userService = userService;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
  {
    taskRegistrar.addCronTask(this::execute, inactivityCheckInterval);
  }

  @Transactional
  protected void execute()
  {
    try {
      log.info("UserInactivityScheduler started");
      deactivateInactiveUsers();
      deleteInactiveUsers();
      log.info("UserInactivityScheduler completed");
    } catch (Exception e) {
      log.error("Error during user inactivity check: {}", e.getMessage(), e);
      throw new BaseException(UNKNOW_ERROR).setReference("Error during user inactivity check");
    }
  }

  private void deactivateInactiveUsers()
  {
    LocalDateTime threshold = LocalDateTime.now().minusDays(inactivityThresholdDays);
    List<User> usersToDeactivate = userService.findWithLastActiveDateBefore(threshold, true);

    log.info("Found {} inactive users inactive since {}", usersToDeactivate.size(), inactivityThresholdDays);

    for (User user : usersToDeactivate) {
      user.setIsActive(false);
      userService.save(user);
      log.info("User {} has been deactivated due to inactivity", user.getId());
    }
  }

  private void deleteInactiveUsers()
  {
    LocalDateTime threshold = LocalDateTime.now().minusDays(deletionThresholdDays);
    List<User> usersToDelete = userService.findWithLastActiveDateBefore(threshold, false);

    log.info("Found {} users to delete", usersToDelete.size());

    for (User user : usersToDelete) {
      userService.delete(user);
      log.info("User {} has been deleted due to inactivity for more than two years", user.getId());
    }
  }
}
