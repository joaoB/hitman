# WaitingTime schema

# --- !Ups
create table `waitingTime` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `bullets` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `crime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `userId` BIGINT,
   FOREIGN KEY (userId) REFERENCES user(id)
)


# --- !Downs
drop table `waitingTime`