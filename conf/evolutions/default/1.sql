# User schema

# --- !Ups
create table `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` TEXT NOT NULL,
  `hp` INT NOT NULL,
  `rp` decimal NOT NULL,
  `bullets` INT NOT NULL
)

# --- !Downs
drop table `user`