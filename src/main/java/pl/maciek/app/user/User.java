package pl.maciek.app.user;

import pl.maciek.app.activities.Activity;
import pl.maciek.app.user.validators.ExtendedEmailValidator;
import pl.maciek.app.user.validators.PostalCodeValidator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExtendedEmailValidator
    @NotBlank(message = "{pl.maciek.app.user.User.userName.NotBlank}")
    private String userName;

    @Size(min = 8, message = "{pl.maciek.app.user.User.password.Size}")
    @NotBlank(message = "{pl.maciek.app.user.User.password.NotBlank}")
    private String password;

    @Size(min = 3, message = "{pl.maciek.app.user.User.firstName.Size}")
    @NotBlank(message = "{pl.maciek.app.user.User.firstName.NotBlank}")
    private String firstName;

    @Size(min = 3, message = "{pl.maciek.app.user.User.lastName.Size}")
    @NotBlank(message = "{pl.maciek.app.user.User.lastName.NotBlank}")
    private String lastName;

    @NotBlank(message = "{pl.maciek.app.user.User.adress.NotBlank}")
    private String adress;

    @PostalCodeValidator(message = "{pl.maciek.app.user.User.postalCode.PostalCodeValidator}")
    @NotBlank(message = "{pl.maciek.app.user.User.postalCode.NotBlank}")
    private String postalCode;

    @NotBlank(message = "{pl.maciek.app.user.User.city.NotBlank}")
    private String city;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Activity> activities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<UserRole> roles;

    public User() {
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
        activity.getParticipants().add(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + userName +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", activities=" + activities.size() +
                ", activities=" + activities +
                '}';
    }
}
