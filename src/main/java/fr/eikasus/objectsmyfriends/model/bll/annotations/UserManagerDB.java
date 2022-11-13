package fr.eikasus.objectsmyfriends.model.bll.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Documented
public @interface UserManagerDB
{
}
