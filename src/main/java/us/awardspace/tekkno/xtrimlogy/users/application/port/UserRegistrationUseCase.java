package us.awardspace.tekkno.xtrimlogy.users.application.port;

import us.awardspace.tekkno.xtrimlogy.commons.Either;
import us.awardspace.tekkno.xtrimlogy.users.domain.UserEntity;

public interface UserRegistrationUseCase {
    RegisterResponse register(String username, String password);

    class RegisterResponse extends Either<String, UserEntity> {

        public RegisterResponse(boolean success, String left, UserEntity right) {
            super(success, left, right);
        }

        public static RegisterResponse success(UserEntity right) {
            return new RegisterResponse(true, null, right);
        }

        public static RegisterResponse failure(String error) {
            return new RegisterResponse(false, error, null);
        }



    }
}
