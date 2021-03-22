package ua.nure.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nure.repository.FloristShopRepository;
import ua.nure.validation.annotation.UniquePhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    @Autowired
    private FloristShopRepository floristShopRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !floristShopRepository.findByPhoneNumber(value).isPresent();
    }
}
