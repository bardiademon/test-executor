create table if not exists `user`
(
    `id`        bigint                                  not null unique primary key auto_increment,
    `firstname` varchar(50) character set 'utf8'        not null,
    `lastname`  varchar(50) character set 'utf8'        not null,
    `username`  varchar(50) character set 'utf8' unique not null,
    `password`  varchar(100)                            not null
);

insert into `user`(`firstname`, `lastname`, `username`, `password`) value ('Bardia', 'Namjoo', 'bardiademon', '73487712');