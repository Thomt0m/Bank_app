package ing.bank.app.models;

import ing.bank.app.entities.User;

/**
 * Used to receive data from client, a transfer object, allowing the client to provide the data required to create a new User entity.
 */
public class NewUser {

    public String Name;

    public String Email;

    public String Password;

    public String Password2;


    public static User toUserEntity(NewUser newUser) {
        return User.builder()
                .fullName(newUser.Name)
                .email(newUser.Email)
                .password(newUser.Password)
                .password2(newUser.Password2)
                .build();
    }

}
