insert into users values (1,"2017-04-08 12:12:12","s3562412@student.rmit.edu.au","Loy", "RMIT","Rao","password");
insert into users values (2,"2017-04-08 12:12:12","admin@dareon.org","Admin","RMIT", "User","admin");
insert into roles values (1,"ROLE_ADMIN");
insert into users_roles values (2,1);
insert into repos values (1, "2017-04-08 12:12:12", "test_definition1","F","test_description1","test_institution1","T","test_repo1",1,2);
insert into proposals values (1, "2017-04-10 12:12:12", "test_cfp_description1", "test_cfp_details1", "test_cfp_institution1", "test_cfp1", 1);