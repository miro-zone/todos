Create AuthenticationController under package controller that expose register endpoint "/api/auth/register".
the register method should accept RegisterRequest object and use AuthenticationService to register the user.

Create a RegisterRequest class under package register that contains the following fields:
firstName: string validate min 2 max 30 and not empty.
lastName: the same as firstName.
email: string validate as valid email, and not empty.
password: string validate as min 5 max 30 not empty.

Create AuthenticationService interface with their implementation under the package service.
The authentication service uses UserRepository and PasswordEncoder to register the user.
The register method is a void method. The registration flow:  
1. check if user already exists.
2. hash the password.
3. save the user.

