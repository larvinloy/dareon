#Add a new systems administrator account

SET @email="sysadmin@rmit.edu.au", @password="password", 
	@first_name="RMIT", @last_name="Systems Admin", @institution="RMIT University";

/*--- do not edit after this line ------------------------------- */
USE dareondb;

INSERT INTO users(email,password,first_name,last_name,institution) 
	VALUES(@email,@password,@first_name,@last_name,@institution);

INSERT INTO users_roles(user_id,role_id) 
	VALUES((SELECT id FROM users WHERE email = @email), (SELECT id FROM roles WHERE name = "ROLE_SA"));

SELECT email,password,first_name,last_name,institution,name AS role 
	FROM users JOIN users_roles ON(users.id = users_roles.user_id)
		JOIN roles ON(users_roles.role_id = roles.id)
			WHERE email=@email;