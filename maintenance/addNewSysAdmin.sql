USE dareondb;
INSERT INTO users VALUES(null,null,"sysadmin@rmit.edu.au","RMIT","RMIT University","Systems Admin","password");
INSERT INTO users_roles values((select id from users where email = "sysadmin@rmit.edu.au"), 2);