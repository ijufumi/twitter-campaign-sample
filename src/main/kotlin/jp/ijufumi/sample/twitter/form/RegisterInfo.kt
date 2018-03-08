package jp.ijufumi.sample.twitter.form

import org.hibernate.validator.constraints.Email

data class RegisterInfo (
        @Email
        var emailAddress: String = ""){
}
