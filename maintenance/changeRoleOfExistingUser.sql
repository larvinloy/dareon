#Change role of an existing user
#Roles: ROLE_SA, ROLE_RO, ROLE_DO, ROLE_PR


SET @email="sysadmin@rmit.edu.au", @role="ROLE_RO";

/*--- do not edit after this line ------------------------------- */
USE dareondb;

UPDATE users_roles SET role_id = (SELECT id FROM roles WHERE name = @role)
	WHERE user_id = (SELECT id FROM users WHERE email = @email);

SELECT email,first_name,last_name,name AS role 
	FROM users JOIN users_roles ON(users.id = users_roles.user_id)
		JOIN roles ON(users_roles.role_id = roles.id)
			WHERE email=@email;