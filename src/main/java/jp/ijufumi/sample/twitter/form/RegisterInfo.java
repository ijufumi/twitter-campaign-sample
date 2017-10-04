package jp.ijufumi.sample.twitter.form;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

@Data
public class RegisterInfo {
    @Email
    String emailAddress;
}
